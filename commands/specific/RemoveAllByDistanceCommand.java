package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;

public class RemoveAllByDistanceCommand extends Command {

    public RemoveAllByDistanceCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
    }

    @Override
    public void execute(String... args) {
        if(routeSet.size() == 0){
            System.out.println("Коллекция пуста.");
        }
        else {
            boolean success = false;
            ArrayList<Route> toDelete = new ArrayList<>();
            for (Route route : routeSet) {
                if (route.getDistance() == Integer.parseInt(args[0])) {
                    toDelete.add(route);
                    success = true;
                }
            }
            for (Route route : toDelete) {
                routeSet.remove(route);
                System.out.println("Удалён маршрут с именем \"" + route.getName() + "\"");
            }
            if (!success) {
                System.out.println("Ни один маршрут не был удалён.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Удалить из коллекции все элементы, дистанция которых равна заданной.";
    }
}
