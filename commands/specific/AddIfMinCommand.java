package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddIfMinCommand extends Command {

    public AddIfMinCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public String execute(String... args) {
        Scanner scanner = new Scanner(System.in);
        AtomicBoolean success = new AtomicBoolean(true);
        Route route = new Route();
        ConsoleReader.readRouteValues(scanner, route);

        route.setId(ConsoleReader.readLongValue(scanner, "id"));

        routeSet.forEach((route1) -> {
            if (route.compareTo(route1) >= 0) {
                success.set(false);
            }
        });


        if (success.get()) {
            routeSet.add(route);
            return  "Новый маршрут успешно добавлен.";
        }
        else {
            return  "Новый маршрут не был добавлен.";
        }
    }
    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию, если его id наименьший.";
    }
}
