package main;

import main.commands.CommandList;
import main.commands.specific.*;
import main.entity.*;
import main.request.Request;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;


public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, SAXException, ParserConfigurationException, XMLStreamException, ClassNotFoundException {


        System.out.println("Консольное приложение по управлению коллекцией маршрутов запущено.");
        RouteSet routeSet = new RouteSet();
        CommandList comList = new CommandList();
        Scanner scanner = new Scanner(System.in);
        ConsoleReader reader = new ConsoleReader(comList, scanner, 10);



        File configFile = new File(args[0]);
        BufferedReader bReader = new BufferedReader(new FileReader(configFile));


        String pathXml = bReader.readLine();
        String pathXsd = bReader.readLine();
        int port = Integer.parseInt(bReader.readLine());


        // Заполнение команд
        fillCommands(routeSet, comList, reader, pathXml);

        // Попытка заполнения коллекции
        if (pathXml != null && pathXsd != null) {
            RouteSetXmlParser.XmlToRouteSet(pathXml, pathXsd, routeSet);
        }


        System.out.println("Введите help для получения справки.");


        // Запуск серверного рабочего
        Thread thread = new Thread(new ServerWorker(reader));
        thread.start();





        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(2048);






        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Подключен клиент: " + client.getRemoteAddress());
                }

                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ObjectInputStream ois;

                    try {
                        client.read(buffer);
                        ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                    }
                    catch (IOException ex) {

                        System.out.println("Отключен клиент: " + client.getRemoteAddress());
                        key.channel().close();
                        selectedKeys.remove(key);
                        client.close();
                        continue;
                    }



                    buffer.flip();


                    Object obj = ois.readObject();
                    Request req = (Request) obj;

                    buffer.clear();

                    byte[] b = RequestHandler.handleRequest(req, routeSet, comList, reader).getBytes();
                    ByteBuffer bb = ByteBuffer.wrap(b);

                    buffer.clear();

                    client.write(bb);

                    ois.close();


                }
                iter.remove();


            }
        }



    }

    private static void fillCommands(RouteSet routeSet, CommandList comList, ConsoleReader reader, String pathXml) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        comList.initCommand(comList, routeSet, "help", HelpCommand.class);
        comList.initCommand(routeSet, "info", InfoCommand.class);
        comList.initCommand(routeSet,"show", ShowCommand.class);
        comList.initCommand(routeSet, "add", AddCommand.class);
        comList.initCommand(routeSet, "update", UpdateCommand.class);
        comList.initCommand(routeSet, "remove_by_id", RemoveByIdCommand.class);
        comList.initCommand(routeSet, "clear", ClearCommand.class);
        comList.initCommand(pathXml, routeSet, "save", SaveCommand.class);
        comList.initCommand(reader, routeSet, "execute_script", ExecuteScriptCommand.class);
        comList.initCommand(pathXml, routeSet, "exit", ExitCommand.class);
        comList.initCommand(routeSet, "add_if_min", AddIfMinCommand.class);
        comList.initCommand(routeSet, "remove_greater", RemoveGreaterCommand.class);
        comList.initCommand(reader, routeSet, "history", HistoryCommand.class);
        comList.initCommand(routeSet, "remove_all_by_distance", RemoveAllByDistanceCommand.class);
        comList.initCommand(routeSet, "filter_contains_name", FilterContainsNameCommand.class);
        comList.initCommand(routeSet, "print_ascending", PrintAscendingCommand.class);
    }
}
