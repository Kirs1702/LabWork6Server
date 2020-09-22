package main.entity;

import main.commands.Command;
import main.commands.CommandList;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleReader {
    private CommandList comList;
    private History history;
    private ScriptHistory scriptHistory = new ScriptHistory();
    private Scanner scanner;

    /**
     * Конструктор - создание истории определённой длины
     * @param comList - список команд
     * @param scanner - используемый сканнер
     * @param historyLength - длина истории комманд
     */
    public ConsoleReader(CommandList comList, Scanner scanner, int historyLength) {
        this.comList = comList;
        history = new History(historyLength);
        this.scanner = scanner;
    }

    /**
     * Метот выделения названия команды из строки
     * @param line строка
     * @return возвращает название команды (первое слово)
     */
    public String prepareCommand(String line) {
        line = line.replaceAll("\\s+", " ").trim();
        try {
            line = (line + " ").split(" ")[0];     //Тут отрезаем первое слово
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return line;
    }

    /**
     * Метот выделения аргументов из строки
     * @param line строка
     * @return возвращает массив аргументов (второе и далее слова)
     */
    public String[] prepareArgs(String line) {
        line = line.replaceAll("\\s+", " ").trim();
        try{
            line = line.substring(line.indexOf(" ")).trim();
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
        return line.split(" ");
    }


    /**
     * Метод выполнения команды
     * @param line строка, по которой определяется команда и её аргументы
     * @throws IOException исключение ввода-вывода
     * @throws XMLStreamException исключение для неожиданных ошибок обработки
     */
    public String executeCommand(String user, String line) throws IOException, XMLStreamException {
        String result = "";
        boolean success = false;
        String[] args = prepareArgs(line);
        String command = prepareCommand(line);


        if (command == null) {
            return "";
        }

        for(Command com : comList) {
            if (com.getName().equals(command)) {
                success = true;
                if (!com.matchesArgs(args)) {
                    result = result.concat("Ошибка при вводе аргументов команды " + com.getName() + ".");
                    break;
                } else {
                    result = result.concat(com.execute(user, args));
                }
                break;
            }
        }


        if (success) {
            history.capture(line);
        } else {
            return "Неизвестная команда: " + command;
        }
        return  result;
    }

    /**
     * Метод чтения комманды из стандартного потока ввода и её выполнения
     * @throws IOException исключение ввода-вывода
     * @throws XMLStreamException исключение для неожиданных ошибок обработки
     */
    public String readCommand(String user) throws IOException, XMLStreamException {
        return executeCommand(user, scanner.nextLine());
    }

    /**
     * Метот вывода истории
     */
    public void showHistory(){
        history.show();
    }

    /**
     * Читает из сканнера строковое значение
     * @param scanner используемый сканнер
     * @param canBeEmpty возможность строки быть пустой
     * @param limit максимальная длина строки, если 0 - неограничена
     * @return возвращает строку, соответствующую формату
     */
    public static String readStringValue(Scanner scanner, boolean canBeEmpty, int limit){
        String line;
        float lenLimit = (limit == 0) ?  Float.POSITIVE_INFINITY : limit;
        while (true) {
            System.out.print("Введите название"  + " (" + (canBeEmpty? "" : "не ") + "может быть пустым" + ((Float.isInfinite(lenLimit)) ? "" : ", максимальная длина: " + (int)lenLimit) + "): ");
            line = scanner.nextLine();
            if ((!canBeEmpty && Pattern.matches(" *", line)) || line.length() > lenLimit) {
                System.out.println("Неверно введено название.");
            } else {
                return Pattern.matches(" *", line) ? null : line.trim();
            }
        }
    }

    /**
     * Читает из сканнера целочисленное значение
     * @param scanner используемый сканнер
     * @param description описание вводного значения
     * @return возвращает целое число типа Integer
     */
    public static Integer readIntValue(Scanner scanner, String  description){
        return readIntValueWithMinimum(scanner, -2147483648, description);
    }

    /**
     * Читает из сканнера целочисленное значение, ограниченное снизу
     * @param scanner используемый сканнер
     * @param minimum минимальное допустимое значение
     * @param description описание вводного значения
     * @return возвращает целое число типа Integer больше минммального значения
     */
    public static Integer readIntValueWithMinimum(Scanner scanner, int minimum, String description){
        String line;
        Integer value = null;
        while (true) {
            System.out.print("Введите " + description + "(целое число, [" + minimum + "...2147483647]): ");
            line = scanner.nextLine();

            try {
                value = Integer.valueOf(line);
            }
            catch (NumberFormatException ignored) {}

            if (value != null && value >= minimum) {
                return  value;
            }
            System.out.println("Введено неверное значение.");
            value = null;
        }
    }

    /**
     * Читает из сканнера число с плавающей точкой
     * @param scanner используемый сканнер
     * @param description описание вводного значения
     * @return возвращает число типа Float
     */
    public static Float readFloatValue(Scanner scanner, String description){
        String line;
        Float value = null;
        while (true) {
            System.out.print("Введите " + description +"(Число с плав. точкой, [ 1.4e-45f...3.4e+38f]): ");
            line = scanner.nextLine();
            try {
                value = Float.valueOf(line);
            }
            catch (NumberFormatException ignored){}
            if (value != null && Float.isFinite(value) && !Float.isNaN(value)) {
                return value;
            }
            System.out.println("Введено неверное значение.");
            value = null;
        }
    }

    /**
     * Читает из сканнера координаты
     * @param scanner используемый сканнер
     * @return возвращает объект типа Coordinates
     */
    public static Coordinates readCoordinates(Scanner scanner){
        return new Coordinates(readFloatValue(scanner, "x"), ConsoleReader.readIntValueWithMinimum(scanner, -862, "y"));
    }
    /**
     * Читает из сканнера локацию
     * @param scanner используемый сканнер
     * @return возвращает объект типа Location
     */
    public static Location readLocation(Scanner scanner){
        return new Location(readIntValue(scanner, "x"), readIntValue(scanner, "y"), readStringValue(scanner, true, 367));
    }

    /**
     * Устанавливает значения, взятые из сканнера, объекту типа Route
     * @param scanner используемый сканнер
     * @param route объект, которому присваиваются значения
     */
    public static  void readRouteValues(Scanner scanner, Route route){
        String line;
        route.setName(ConsoleReader.readStringValue(scanner, false, 0));

        System.out.println("Координаты маршрута:");

        route.setCoordinates(ConsoleReader.readCoordinates(scanner));

        System.out.println("Координаты маршрута:");

        while (true) {
            System.out.println("Желаете добавить стартовую локацию?[y/n]: ");
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("y")) {
                System.out.println("Стартовая локация: ");
                route.setFrom(ConsoleReader.readLocation(scanner));
                break;
            }
            else if (line.equalsIgnoreCase("n")) {
                break;
            }
        }

        System.out.println("Конечная локация: ");
        route.setTo(ConsoleReader.readLocation(scanner));

        route.setDistance(ConsoleReader.readIntValueWithMinimum(scanner, 2, "протяженность"));
    }

    /**
     * Читает из сканнера целочисленное значение
     * @param scanner используемый сканнер
     * @return возвращает целое число типа Long
     */
    public static Long readLongPositiveValue(Scanner scanner, String description){
        String line;
        Long value = null;

        while (true) {
            System.out.print("Введите " + description + " (целое число, [1...9223372036854775807L]): ");
            line = scanner.nextLine();

            try {
                value = Long.valueOf(line);
            }
            catch (NumberFormatException ignored) {}

            if (value != null && value > 0) {
                return  value;
            }

            System.out.println("Введено неверное значение.");
            value = null;
        }
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ScriptHistory getScriptHistory() {
        return scriptHistory;
    }
}
