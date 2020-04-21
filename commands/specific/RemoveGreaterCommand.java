package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;
import java.util.Scanner;

public class RemoveGreaterCommand extends Command {

    public RemoveGreaterCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        Route route = new Route();
        System.out.println("Удаление элементов, id которых больше заданного:");
        System.out.println("Введите id элемента:");
        route.setId(ConsoleReader.readLongValue(scanner));



        ArrayList<Route> toDelete = new ArrayList<>();
        for(Route routes : routeSet) {
            if (routes.compareTo(route) > 0) {
                toDelete.add(routes);
                success = true;
            }
        }

        for (Route routes : toDelete) {
            routeSet.remove(routes);
            System.out.println("Удалён маршрут с именем \"" + routes.getName() + "\"");
        }
        if (!success) {
            System.out.println("Ни один маршрут не был удалён.");
        }

    }

    @Override
    public String getDescription() {
        return "Удалить из коллекции все элементы, превышающие заданный.";
    }
}
