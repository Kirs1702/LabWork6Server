package main.commands.specific;

import main.commands.Command;
import main.entity.*;

import java.util.Scanner;

public class AddCommand extends Command {

    public AddCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public String execute(String... args) {
        Scanner scanner = new Scanner(System.in);
        Route route = new Route();
        ConsoleReader.readRouteValues(scanner, route);
        routeSet.add(route);
        return  "Новый маршрут успешно добавлен.";
    }



    @Override
    public String getDescription() {
        return "Добавить новый элемент.";
    }
}
