package main;

import main.commands.CommandList;
import main.commands.specific.*;
import main.entity.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, SAXException, ParserConfigurationException, XMLStreamException {


        System.out.println("Консольное приложение по управлению коллекцией маршрутов запущено.");
        RouteSet routeSet = new RouteSet();
        CommandList comList = new CommandList();
        Scanner scanner = new Scanner(System.in);
        ConsoleReader reader = new ConsoleReader(comList, scanner, 10);

        // Считывание переменных окружения
        String pathXml = EnvironmentGetter.getEnv("LAB_INPUT_PATH");
        String pathXsd = EnvironmentGetter.getEnv("LAB_SCHEMA_PATH");

        // Заполнение команд
        fillCommands(routeSet, comList, reader, pathXml);

        // Попытка заполнения коллекции
        if (pathXml != null && pathXsd != null) {
            RouteSetXmlParser.XmlToRouteSet(pathXml, pathXsd, routeSet);
        }
        System.out.println("Введите help для получения справки.");


        while (true){
            try {
                reader.readCommand();
            }
            catch (NoSuchElementException e) {
                System.out.println("Работа завершена.");
                System.exit(0);
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
        comList.initCommand(routeSet, "exit", ExitCommand.class);
        comList.initCommand(routeSet, "add_if_min", AddIfMinCommand.class);
        comList.initCommand(routeSet, "remove_greater", RemoveGreaterCommand.class);
        comList.initCommand(reader, routeSet, "history", HistoryCommand.class);
        comList.initCommand(routeSet, "remove_all_by_distance", RemoveAllByDistanceCommand.class);
        comList.initCommand(routeSet, "filter_contains_name", FilterContainsNameCommand.class);
        comList.initCommand(routeSet, "print_ascending", PrintAscendingCommand.class);
    }
}
