package main.entity;

import main.commands.CommandList;
import main.request.LoginReq;
import main.request.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
;
import java.util.concurrent.*;

public class HandlerThread extends Thread {
    private final SocketChannel client;
    private final Selector selector;
    private final DataBaseHandler dbh;
    private final RouteSet routeSet;
    private final CommandList comList;
    private final ForkJoinPool fjp;
    private final ExecutorService ctp;

    public HandlerThread(SocketChannel client, Selector selector, DataBaseHandler dbh, RouteSet routeSet, CommandList comList, ForkJoinPool fjp, ExecutorService ctp) {
        this.client = client;
        this.selector = selector;
        this.dbh = dbh;
        this.routeSet = routeSet;
        this.comList = comList;
        this.fjp = fjp;
        this.ctp = ctp;
    }

    public void run() {
        try {
            System.out.println("Подключен клиент " + client.getRemoteAddress());
            ByteBuffer buffer = ByteBuffer.allocate(2048);

            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            String username;


            finish:
            {
            outerLoop:
            while (true) {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isReadable()) {
                        ObjectInputStream ois;
                        try {
                            client.read(buffer);
                            ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                        } catch (IOException ex) {
                            iter.remove();
                            break finish;
                        }
                        buffer.flip();
                        Object obj = ois.readObject();
                        LoginReq req = (LoginReq) obj;
                        buffer.clear();

                        if (req.isRegister()) {

                            if (req.getUsername().equals("master")) {
                                byte[] b = {2};
                                client.write(ByteBuffer.wrap(b));
                                break;
                            }

                            if (dbh.register(req.getUsername(), req.getPassword())) {
                                byte[] b = {1};
                                client.write(ByteBuffer.wrap(b));
                                ois.close();
                                username = req.getUsername();
                                break outerLoop;
                            }
                            else {
                                byte[] b = {2};
                                client.write(ByteBuffer.wrap(b));
                                break;
                            }

                        }




                        else {


                            if (dbh.login(req.getUsername(), req.getPassword())) {
                                byte[] b = {1};
                                client.write(ByteBuffer.wrap(b));
                                ois.close();
                                username = req.getUsername();
                                break outerLoop;
                            }
                            else {
                                byte[] b = {2};
                                client.write(ByteBuffer.wrap(b));
                                break;
                            }


                        }




                    }
                    iter.remove();

                }

            }


            buffer.clear();

            outerLoop:
            while (true) {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isReadable()) {
                        ObjectInputStream ois;

                        try {
                            client.read(buffer);
                            ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                        } catch (IOException ex) {
                            break outerLoop;
                        }
                        buffer.flip();
                        Object obj = ois.readObject();
                        Request req = (Request) obj;
                        buffer.clear();


                        Callable<ByteBuffer> c = () -> {
                            byte[] b = RequestHandler.handleRequest(username, dbh, req, routeSet, comList).getBytes();
                            return ByteBuffer.wrap(b);
                        };

                        ForkJoinTask<ByteBuffer> task = ForkJoinTask.adapt(c);
                        ByteBuffer bb = fjp.invoke(task);

                        buffer.clear();



                        Runnable r = () -> {
                            try {
                                client.write(bb);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        };

                        Future future = ctp.submit(r);
                        while (!future.isDone());


                        ois.close();
                    }
                    iter.remove();

                }

            }
            }
            System.out.println("Отключен клиент " + client.getRemoteAddress());
            client.close();

        }

        catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
