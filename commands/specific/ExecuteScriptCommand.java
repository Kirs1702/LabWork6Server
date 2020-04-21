package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.RouteSet;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.Scanner;


public class ExecuteScriptCommand extends Command {
    private ConsoleReader consoleReader;
    public ExecuteScriptCommand(ConsoleReader consoleReader, RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, ".+");
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String... args) throws IOException, XMLStreamException {
        Scanner fileScan;
        try {
            fileScan = new Scanner(new InputStreamReader(new FileInputStream(new File(args[0]))));
        } catch (FileNotFoundException e) {
            System.out.println("Скрипт " + args[0] + " не существует или недоступен к чтению. Скрипт не выполнен.");
            return;
        }


        System.out.println("Начало выполнения скрипта " + args[0]);
        //Замена сканнеров у консолРидера
        Scanner prevScanner = consoleReader.getScanner();
        consoleReader.setScanner(fileScan);
        //


        while (true) {
            if (consoleReader.getScanner().hasNextLine()) {
                String line = consoleReader.getScanner().nextLine();
                if (consoleReader.prepareCommand(line).equals("execute_script")) {
                    if (consoleReader.getScriptHistory().put(consoleReader.prepareArgs(line)[0])) {
                        consoleReader.executeCommand(line);
                        consoleReader.getScriptHistory().pop();
                    } else {
                        System.out.println("Скрипт " + consoleReader.prepareArgs(line)[0] + " не выполняется, так как он приведёт к бесконечной рекурсии.");
                    }
                }
                else {
                    consoleReader.executeCommand(line);
                }

            } else {
                System.out.println("Конец выполнения скрипта " + args[0]);
                break;
            }




        }
            //Замена сканнеров у консолРидера
            consoleReader.setScanner(prevScanner);
            //



    }





    @Override
    public String getDescription() {
        return "Считать и исполнить скрипт из указанного файла.";
    }
}
