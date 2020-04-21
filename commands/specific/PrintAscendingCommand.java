package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.ArrayList;

public class PrintAscendingCommand extends Command {

    public PrintAscendingCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {


        if (routeSet.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        Route routeMin = new Route();
        ArrayList<Route> toPrint = new ArrayList<>();
        routeMin.setId(9223372036854775807L);

        for(Route route : routeSet){
            if (route.compareTo(routeMin) < 0) {
                routeMin = route;
            }
        }
        toPrint.add(routeMin);

        for (int i = 1; i < routeSet.size(); i++){
            toPrint.add(new Route());
            toPrint.get(i).setId((9223372036854775807L));
            for(Route route : routeSet){
                if (route.compareTo(toPrint.get(i)) < 0 && route.compareTo(toPrint.get(i-1)) > 0) {
                    toPrint.set(i, route);
                }
            }
        }







        for(Route route : toPrint){
            System.out.println(route.toString());
        }
    }

    @Override
    public String getDescription() {
        return "Вывести элементы коллекции в порядке возрастания.";
    }
}
