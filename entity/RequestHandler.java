package main.entity;


import main.commands.CommandList;
import main.commands.specific.*;
import main.request.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class RequestHandler {




    public static String handleRequest(Request request, RouteSet routeSet, CommandList comList, ConsoleReader conReader) throws IOException, XMLStreamException {


        switch (request.getName()) {

            case "add_if_min" : {
                AddIfMinReq newReq = (AddIfMinReq) request;
                Route route;

                if (!newReq.isNoAddFrom()){
                    route = new Route(newReq.getNewId(), newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                            new Location(newReq.getFromX(), newReq.getFromY(), newReq.getFromName()), new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                } else  {
                    route = new Route(newReq.getNewId(), newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                             new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                }
                for(Route routes : routeSet) {
                    if (route.compareTo(routes) == 0) {
                        return  "Новый маршрут не был добавлен.";
                    }
                }

                for(Route routes : routeSet) {
                    if (route.compareTo(routes) >= 0) {
                        return  "Новый маршрут не был добавлен.";
                    }
                }
                routeSet.add(route);
                return  "Новый маршрут успешно добавлен.";

            }

            case "add" : {
                AddReq newReq = (AddReq) request;
                Route route;

                if (!newReq.isNoAddFrom()){
                    route = new Route(newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                            new Location(newReq.getFromX(), newReq.getFromY(), newReq.getFromName()), new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                } else  {
                    route = new Route(newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                            new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                }

                routeSet.add(route);
                return  "Новый маршрут успешно добавлен.";
            }

            case "clear" : {
                routeSet.clear();
                return "Коллекция очищена.";
            }

            case "filter_contains_name" : {
                FilterContainsNameReq newReq = (FilterContainsNameReq) request;
                return new FilterContainsNameCommand(routeSet, "").execute(newReq.getRouteName());
            }

            case "help" : {
                return new HelpCommand(comList, routeSet, "").execute();
            }

            case "info" : {
                return new InfoCommand(routeSet, "").execute();
            }

            case "print_ascending" : {
                return new PrintAscendingCommand(routeSet, "").execute();
            }

            case "remove_all_by_distance" : {
                RemoveAllByDistanceReq newReq = (RemoveAllByDistanceReq) request;
                return new RemoveAllByDistanceCommand(routeSet, "").execute(Integer.toString(newReq.getDistance()));
            }

            case "remove_by_id" : {
                RemoveByIdReq newReq = (RemoveByIdReq) request;
                return new RemoveByIdCommand(routeSet, "").execute(Long.toString(newReq.getId()));
            }

            case "remove_greater" : {
                RemoveGreaterReq newReq = (RemoveGreaterReq) request;
                return new RemoveGreaterCommand(routeSet, "", newReq.getId()).execute();
            }

            case "show" : {
                return new ShowCommand(routeSet, "").execute();
            }

            case "update" : {
                UpdateReq newReq = (UpdateReq) request;


                for (Route route : routeSet) {
                    if (route.getId() == newReq.getId()) {

                        if (!newReq.isNoAddFrom()){
                            route = new Route(newReq.getId(), newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                                    new Location(newReq.getFromX(), newReq.getFromY(), newReq.getFromName()), new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                        } else  {
                            route = new Route(newReq.getId(), newReq.getRouteName(), new Coordinates(newReq.getCordX(), newReq.getCordY()),
                                    new Location(newReq.getToX(),newReq.getToY(), newReq.getToName()), newReq.getDistance());
                        }

                        return "Значения успешно обновлены.";

                    }
                }

                return "Маршрут с данным id отсутствует в коллекции.";
            }


            default : {
                return "Хмм, как эта команда сюда попала....";
            }

        }


    }


}
