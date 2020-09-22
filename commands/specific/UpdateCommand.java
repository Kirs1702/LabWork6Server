package main.commands.specific;

import main.commands.Command;
import main.entity.*;
import main.request.UpdateReq;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class UpdateCommand extends Command {

    private DataBaseHandler dbh;
    public UpdateCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
        this.dbh = dbh;
    }

    @Override
    public String execute(String user, String... args) {

        for (Route route : routeSet.getSet()) {
            if (route.getId() == Long.parseLong(args[0])) {

                Route route1 = new Route();
                ConsoleReader.readRouteValues(new Scanner(System.in), route1);
                route1.setUser(route.getUser());
                route1.setId(route.getId());
                route1.setCreationDate(route.getCreationDate());
                dbh.setRouteById(route1, route.getId());
                route.copyRoute(route1);


                dbh.setRouteById(route, Long.parseLong(args[0]));
                return "Значения успешно обновлены.";
            }
        }
        return "Маршрут с данным id отсутствует в коллекции.";

    }

    public String execute(String user, UpdateReq updateReq){
        for (Route route : routeSet.getSet()) {
            if (route.getId() == updateReq.getId()) {
                if (!user.equals(route.getUser())){
                    return "Вы не являетесь владельцем данного маршрута.";
                }
                Route route1;
                if (!updateReq.isNoAddFrom()){
                    route1 = new Route(updateReq.getId(), updateReq.getRouteName(), new Coordinates(updateReq.getCordX(), updateReq.getCordY()),
                            new Location(updateReq.getFromX(), updateReq.getFromY(), updateReq.getFromName()), new Location(updateReq.getToX(), updateReq.getToY(), updateReq.getToName()), updateReq.getDistance());
                } else  {
                    route1 = new Route(updateReq.getId(), updateReq.getRouteName(), new Coordinates(updateReq.getCordX(), updateReq.getCordY()),
                            new Location(updateReq.getToX(), updateReq.getToY(), updateReq.getToName()), updateReq.getDistance());
                }
                route1.setUser(route.getUser());
                route1.setId(route.getId());
                route1.setCreationDate(route.getCreationDate());
                dbh.setRouteById(route1, route.getId());
                route.copyRoute(route1);

                return "Значения успешно обновлены.";

            }
        }

        return "Маршрут с данным id отсутствует в коллекции.";
    }

    @Override
    public String getDescription() {
        return "Обновить значение элемента коллекции по id.";
    }
}
