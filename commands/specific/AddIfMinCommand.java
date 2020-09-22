package main.commands.specific;

import main.commands.Command;
import main.entity.*;
import main.request.AddIfMinReq;
import main.request.AddReq;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddIfMinCommand extends Command {

    private DataBaseHandler dbh;
    public AddIfMinCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        this.dbh = dbh;
    }

    @Override
    public String execute(String user, String... args) {
        Scanner scanner = new Scanner(System.in);
        AtomicBoolean success = new AtomicBoolean(true);
        Route route = new Route();
        ConsoleReader.readRouteValues(scanner, route);

        route.setId(ConsoleReader.readLongPositiveValue(scanner, "id"));
        route.setUser(user);

        routeSet.getSet().forEach((route1) -> {
            if (route.compareTo(route1) >= 0) {
                success.set(false);
            }
        });


        if (success.get()) {
            dbh.insertRoute(route, route.getId());
            routeSet.add(route);
            return  "Новый маршрут успешно добавлен.";
        }
        else {
            return  "Новый маршрут не был добавлен.";
        }
    }


    public String execute(String user, AddIfMinReq addIfMinReq) {
        Route route;

        if (!addIfMinReq.isNoAddFrom()){
            route = new Route(addIfMinReq.getNewId(), addIfMinReq.getRouteName(), new Coordinates(addIfMinReq.getCordX(), addIfMinReq.getCordY()),
                    new Location(addIfMinReq.getFromX(), addIfMinReq.getFromY(), addIfMinReq.getFromName()), new Location(addIfMinReq.getToX(),addIfMinReq.getToY(), addIfMinReq.getToName()), addIfMinReq.getDistance());
        } else  {
            route = new Route(addIfMinReq.getNewId(), addIfMinReq.getRouteName(), new Coordinates(addIfMinReq.getCordX(), addIfMinReq.getCordY()),
                    new Location(addIfMinReq.getToX(),addIfMinReq.getToY(), addIfMinReq.getToName()), addIfMinReq.getDistance());
        }
        route.setUser(user);
        for(Route routes : routeSet.getSet()) {
            if (route.compareTo(routes) == 0) {
                return  "Новый маршрут не был добавлен.";
            }
        }

        for(Route routes : routeSet.getSet()) {
            if (route.compareTo(routes) >= 0) {
                return  "Новый маршрут не был добавлен.";
            }
        }
        dbh.insertRoute(route, route.getId());
        routeSet.add(route);
        return  "Новый маршрут успешно добавлен.";
    }

    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию, если его id наименьший.";
    }
}
