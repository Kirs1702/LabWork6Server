package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

public class RemoveByIdCommand extends Command {

    public RemoveByIdCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
    }

    @Override
    public String execute(String... args) {

        if(routeSet.size() == 0){
            return "Коллекция пуста.";
        }
        else {
            for (Route route : routeSet) {
                if (route.getId() == Long.parseLong(args[0])) {
                    routeSet.remove(route);
                    return "Удалён маршрут с именем \"" + route.getName() + "\"";
                }
            }

            return "Маршрут с данным id отсутствует в коллекции.";

        }

    }

    @Override
    public String getDescription() {
        return "Удалить элемент из коллекции по id.";
    }
}
