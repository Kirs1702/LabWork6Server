package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;
import java.util.Scanner;

public class RemoveGreaterCommand extends Command {

    private Long id;

    public RemoveGreaterCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        id = null;
    }

    public RemoveGreaterCommand(RouteSet routeSet, String name, Long id) {
        super(routeSet, name);
        this.id  = id;
    }

    @Override
    public String execute(String... args) {

        String result = "";

        boolean success = false;
        Route route = new Route();
        if (id == null) {
            route.setId(ConsoleReader.readLongValue(new Scanner(System.in), "id"));
        }
        else {
            route.setId(id);
        }



        ArrayList<Route> toDelete = new ArrayList<>();
        for(Route routes : routeSet) {
            if (routes.compareTo(route) > 0) {
                toDelete.add(routes);
                success = true;
            }
        }

        for (Route routes : toDelete) {
            routeSet.remove(routes);
            result = result.concat("Удалён маршрут с именем \"" + routes.getName() + "\"\n");
        }
        if (!success) {
            return "Ни один маршрут не был удалён.";
        }
        return result;

    }

    @Override
    public String getDescription() {
        return "Удалить из коллекции все элементы, превышающие заданный.";
    }
}
