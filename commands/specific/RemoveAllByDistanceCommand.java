package main.commands.specific;

import main.commands.Command;
import main.entity.DataBaseHandler;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;

public class RemoveAllByDistanceCommand extends Command {

    private DataBaseHandler dbh;
    public RemoveAllByDistanceCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
        this.dbh = dbh;
    }

    @Override
    public String  execute(String user, String... args) {
        String result = "";
        boolean success = false;
        if(routeSet.size() == 0){
            return "Коллекция пуста.";
        }
        else {

            ArrayList<Route> toDelete = new ArrayList<>();
            for(Route route : routeSet.getSet()) {
                if (route.getDistance() == Integer.parseInt(args[0])) {
                    if (user.equals(route.getUser()) || user.equals("master")){
                        toDelete.add(route);
                    }
                }
            }

            for (Route route : toDelete) {
                dbh.deleteRouteById(route.getId());
                routeSet.remove(route);
                result = result.concat("Удалён маршрут с именем \"" + route.getName() + "\"\n");
                success = true;


            }
        }
        if (success) return result;
        return  "Не был удалён ни один элемент";
    }

    @Override
    public String getDescription() {
        return "Удалить элементы, дистанция которых равна заданной.";
    }
}
