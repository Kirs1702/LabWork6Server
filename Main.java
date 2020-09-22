package main;

import main.commands.CommandList;
import main.commands.specific.*;
import main.entity.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException, NoSuchAlgorithmException {

        RouteSet routeSet = new RouteSet();






        CommandList comList = new CommandList();
        Scanner scanner = new Scanner(System.in);
        ConsoleReader reader = new ConsoleReader(comList, scanner, 10);
        DataBaseHandler dbh = null;

        int port = 0;
        try {

            File configFile = new File(args[0]);
            BufferedReader bReader = new BufferedReader(new FileReader(configFile));

            port = Integer.parseInt(bReader.readLine());

            //----------------------------

            dbh = new DataBaseHandler("jdbc:postgresql://localhost:5432/RoutesInfo", "postgres", "pass1word");
            routeSet = dbh.selectAllRoutes();


            //----------------------------

            // Заполнение команд
            fillCommands(routeSet, comList, reader, dbh);

        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Требуется один аргумент коммандной строки!");
            System.exit(0);
        }
        catch (FileNotFoundException ex) {
            System.out.println("Файл не найден!");
            System.exit(0);
        }
        catch (NumberFormatException ex){
            System.out.println("В файле неверно указан порт!");
            System.exit(0);
        }

        Thread thread = new Thread(new ServerWorker(reader));
        thread.start();



        System.out.println("Консольное приложение по управлению коллекцией маршрутов запущено.\nВведите help для получения справки.");





        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);


        ForkJoinPool fjp = ForkJoinPool.commonPool();
        ExecutorService ctp = Executors.newCachedThreadPool();






        while (true) {

            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {


                    Selector newSelector = Selector.open();
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);
                    client.register(newSelector, SelectionKey.OP_READ);

                    new HandlerThread(client, newSelector, dbh, routeSet, comList, fjp, ctp).start();
                }

                iter.remove();


            }
        }



    }

    private static void fillCommands(RouteSet routeSet, CommandList comList, ConsoleReader reader, DataBaseHandler dbh) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        comList.initCommand(comList, routeSet, "help", HelpCommand.class);
        comList.initCommand(routeSet, "info", InfoCommand.class);
        comList.initCommand(routeSet,"show", ShowCommand.class);
        comList.initCommand(routeSet, "add", dbh, AddCommand.class);
        comList.initCommand(routeSet, "update", dbh, UpdateCommand.class);
        comList.initCommand(routeSet, "remove_by_id", dbh, RemoveByIdCommand.class);
        comList.initCommand(routeSet, "clear", dbh, ClearCommand.class);
        //comList.initCommand(pathXml, routeSet, "save", SaveCommand.class);
        comList.initCommand(reader, routeSet, "execute_script", ExecuteScriptCommand.class);
        comList.initCommand(routeSet, "exit", ExitCommand.class);
        comList.initCommand(routeSet, "add_if_min", dbh, AddIfMinCommand.class);
        comList.initCommand(routeSet, "remove_greater", dbh, RemoveGreaterCommand.class);
        comList.initCommand(reader, routeSet, "history", HistoryCommand.class);
        comList.initCommand(routeSet, "remove_all_by_distance", dbh,  RemoveAllByDistanceCommand.class);
        comList.initCommand(routeSet, "filter_contains_name", FilterContainsNameCommand.class);
        comList.initCommand(routeSet, "print_ascending", PrintAscendingCommand.class);
    }
}
