package main.commands.specific;

import main.commands.Command;
import main.entity.DataBaseHandler;
import main.entity.Route;
import main.entity.RouteSet;

public class RemoveByIdCommand extends Command {

    DataBaseHandler dbh;
    public RemoveByIdCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
        this.dbh = dbh;
    }

    @Override
    public String execute(String user, String... args) {

        if(routeSet.size() == 0){
            return "Коллекция пуста.";
        }
        else {
            for (Route route : routeSet.getSet()) {
                if (route.getId() == Long.parseLong(args[0])) {
                    if (user.equals(route.getUser()) || user.equals("master")){
                        dbh.deleteRouteById(Long.parseLong(args[0]));
                        routeSet.remove(route);
                        return "Удалён маршрут с именем \"" + route.getName() + "\"";
                    }
                    else return "Вы не хозяин";
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
