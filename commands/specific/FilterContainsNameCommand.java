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
    public void execute(String... args) {
        boolean success = false;
        for (Route route : routeSet) {
            if (route.getName().contains(args[0])) {
                System.out.println(route.toString());
                success = true;
            }
        }
        if (!success) {
            System.out.println("Маршрут с данным полем name отсутствует в коллекции.");
        }
    }

    @Override
    public String getDescription() {
        return "Вывести элементы, значение поля name которых содержит заданную подстроку.";
    }
}
