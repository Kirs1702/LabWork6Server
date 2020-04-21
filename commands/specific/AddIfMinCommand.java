package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.Scanner;

public class AddIfMinCommand extends Command {

    public AddIfMinCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добавление нового маршрута в коллекцию:");
        boolean success = true;
        Route route = new Route();
        ConsoleReader.readRouteValues(scanner, route);

        System.out.println("Введите id элемента (элемент будет добавлен, если данный id наименьший из имеющихся в коллекции):");
        while(true) {
            success = true;
            route.setId(ConsoleReader.readLongValue(scanner));
            for(Route routes : routeSet) {
                if (route.compareTo(routes) == 0) {
                    System.out.println("В коллекции уже имеется элемент с данным id. Введите другое значение.");
                    success = false;
                    break;
                }
            }
            if(success) {
                break;
            }
        }
        for(Route routes : routeSet) {
            if (route.compareTo(routes) >= 0) {
                success = false;
                break;
            }
        }
        if (success) {
            routeSet.add(route);
            System.out.println("Новый маршрут успешно добавлен.");
        }
        else {
            System.out.println("Новый маршрут не был добавлен.");
        }
    }
    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.";
    }
}
