package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.DataBaseHandler;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;
import java.util.Scanner;

public class RemoveGreaterCommand extends Command {

    private DataBaseHandler dbh;
    private Long id;

    public RemoveGreaterCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        id = null;
        this.dbh = dbh;
    }

    public RemoveGreaterCommand(RouteSet routeSet, String name, DataBaseHandler dbh, Long id) {
        super(routeSet, name);
        this.id  = id;
        this.dbh = dbh;
    }

    @Override
    public String execute(String user, String... args) {

        String result = "";

        boolean success = false;
        Route route = new Route();
        if (id == null) {
            route.setId(ConsoleReader.readLongPositiveValue(new Scanner(System.in), "id"));
        }
        else {
            route.setId(id);
        }


        ArrayList<Route> toDelete = new ArrayList<>();
        for(Route routes : routeSet.getSet()) {
            if (routes.compareTo(route) > 0) {
                toDelete.add(routes);
            }
        }

        for (Route route1 : toDelete) {

            if (user.equals(route1.getUser()) || user.equals("master")){
                dbh.deleteRouteById(route1.getId());
                routeSet.remove(route1);
                result = result.concat("Удалён маршрут с именем \"" + route1.getName() + "\"\n");
                success = true;
            }

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
