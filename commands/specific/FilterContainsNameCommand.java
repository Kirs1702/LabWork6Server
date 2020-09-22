package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

public class FilterContainsNameCommand extends Command {


    public FilterContainsNameCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, ".*");
    }

    @Override
    public String  execute(String user, String... args) {
        String result = "";
        boolean success = false;
        for (Route route : routeSet.getSet()) {
            if (route.getName().contains(args[0])) {
                result = result.concat(route.toString());
                result = result.concat("\n");
                success = true;
            }
        }
        if (!success) {
            return "Маршрут с данным полем name отсутствует в коллекции.";
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "Вывести элементы, значение поля name которых содержит заданную подстроку.";
    }
}
