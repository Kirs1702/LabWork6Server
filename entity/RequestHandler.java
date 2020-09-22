package main.entity;


import main.commands.CommandList;
import main.commands.specific.*;
import main.request.*;

public class RequestHandler {




    public static String handleRequest(String user, DataBaseHandler dbh, Request request, RouteSet routeSet, CommandList comList) {


        switch (request.getName()) {

            case "add_if_min" : {
                AddIfMinReq newReq = (AddIfMinReq) request;
                return  new AddIfMinCommand(routeSet, "", dbh).execute(user, newReq);
            }

            case "add" : {
                AddReq newReq = (AddReq) request;
                return  new AddCommand(routeSet, "", dbh).execute(user, newReq);
            }

            /*case "clear" : {
                return new ClearCommand(routeSet, "").execute(user);
            }*/

            case "filter_contains_name" : {
                FilterContainsNameReq newReq = (FilterContainsNameReq) request;
                return new FilterContainsNameCommand(routeSet, "").execute(newReq.getRouteName());
            }

            case "help" : {
                return new HelpCommand(comList, routeSet, "").execute(user);
            }

            case "info" : {
                return new InfoCommand(routeSet, "").execute(user);
            }

            case "print_ascending" : {
                return new PrintAscendingCommand(routeSet, "").execute(user);
            }

            case "remove_all_by_distance" : {
                RemoveAllByDistanceReq newReq = (RemoveAllByDistanceReq) request;
                return new RemoveAllByDistanceCommand(routeSet, "", dbh).execute(user, Integer.toString(newReq.getDistance()));
            }

            case "remove_by_id" : {
                RemoveByIdReq newReq = (RemoveByIdReq) request;
                return new RemoveByIdCommand(routeSet, "", dbh).execute(user, Long.toString(newReq.getId()));
            }

            case "remove_greater" : {
                RemoveGreaterReq newReq = (RemoveGreaterReq) request;
                return new RemoveGreaterCommand(routeSet, "", dbh, newReq.getId()).execute(user);
            }

            case "show" : {
                return new ShowCommand(routeSet, "").execute(user);
            }

            case "update" : {
                UpdateReq newReq = (UpdateReq) request;
                return new UpdateCommand(routeSet, "", dbh).execute(user, newReq);
            }


            default : {
                return "Хмм, как эта команда сюда попала....";
            }

        }


    }


}
