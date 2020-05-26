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
    public String  execute(String... args) {

        if(routeSet.size() == 0){
            return "Коллекция пуста.";
        }
        else {
            routeSet.removeIf((route) -> route.getDistance() == Integer.parseInt(args[0]));
        }
        return "Были удалены элементы коллекции.";
    }

    @Override
    public String getDescription() {
        return "Удалить элементы, дистанция которых равна заданной.";
    }
}
