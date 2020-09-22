package main.commands.specific;

import main.commands.Command;
import main.entity.*;
import main.request.AddReq;

import java.util.Scanner;

public class AddCommand extends Command {

    private DataBaseHandler dbh;

    public AddCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        this.dbh = dbh;
    }

    @Override
    public String execute(String user, String... args) {
        Scanner scanner = new Scanner(System.in);
        Route route = new Route();
        ConsoleReader.readRouteValues(scanner, route);
        route.setUser(user);
        route.setId(dbh.insertRouteReturnId(route));
        routeSet.add(route);
        return  "Новый маршрут успешно добавлен.";
    }

    public String execute(String user, AddReq addReq) {
        Route route;

        if (!addReq.isNoAddFrom()){
            route = new Route(addReq.getRouteName(), new Coordinates(addReq.getCordX(), addReq.getCordY()),
                    new Location(addReq.getFromX(), addReq.getFromY(), addReq.getFromName()), new Location(addReq.getToX(),addReq.getToY(), addReq.getToName()), addReq.getDistance());
        } else  {
            route = new Route(addReq.getRouteName(), new Coordinates(addReq.getCordX(), addReq.getCordY()),
                    new Location(addReq.getToX(),addReq.getToY(), addReq.getToName()), addReq.getDistance());
        }
        route.setUser(user);
        route.setId(dbh.insertRouteReturnId(route));

        routeSet.add(route);
        return  "Новый маршрут успешно добавлен.";
    }



    @Override
    public String getDescription() {
        return "Добавить новый элемент.";
    }
}
