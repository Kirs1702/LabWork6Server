package main.entity;


import main.commands.CommandList;
import main.commands.specific.*;
import main.request.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RequestHandler {




    public static byte[] handleRequest(String user, DataBaseHandler dbh, Request request, RouteSet routeSet, CommandList comList) throws IOException {


        switch (request.getName()) {

            case "add_if_min" : {
                AddIfMinReq newReq = (AddIfMinReq) request;
                return  new AddIfMinCommand(routeSet, "", dbh).execute(user, newReq).getBytes();
            }

            case "add" : {
                AddReq newReq = (AddReq) request;
                return  new AddCommand(routeSet, "", dbh).execute(user, newReq).getBytes();
            }

            /*case "clear" : {
                return new ClearCommand(routeSet, "").execute(user);
            }*/

            case "filter_contains_name" : {
                FilterContainsNameReq newReq = (FilterContainsNameReq) request;
                return new FilterContainsNameCommand(routeSet, "").execute(user, newReq.getRouteName()).getBytes();
            }

            case "help" : {
                return new HelpCommand(comList, routeSet, "").execute(user).getBytes();
            }

            case "info" : {
                return new InfoCommand(routeSet, "").execute(user).getBytes();
            }

            case "print_ascending" : {
                return new PrintAscendingCommand(routeSet, "").execute(user).getBytes();
            }

            case "remove_all_by_distance" : {
                RemoveAllByDistanceReq newReq = (RemoveAllByDistanceReq) request;
                return new RemoveAllByDistanceCommand(routeSet, "", dbh).execute(user, Integer.toString(newReq.getDistance())).getBytes();
            }

            case "remove_by_id" : {
                RemoveByIdReq newReq = (RemoveByIdReq) request;
                return new RemoveByIdCommand(routeSet, "", dbh).execute(user, Long.toString(newReq.getId())).getBytes();
            }

            case "remove_greater" : {
                RemoveGreaterReq newReq = (RemoveGreaterReq) request;
                return new RemoveGreaterCommand(routeSet, "", dbh, newReq.getId()).execute(user).getBytes();
            }

            case "show" : {
                return new ShowCommand(routeSet, "").execute(user).getBytes();
            }

            case "update" : {
                UpdateReq newReq = (UpdateReq) request;
                return new UpdateCommand(routeSet, "", dbh).execute(user, newReq).getBytes();
            }
            case "set" : {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(routeSet);
                return baos.toByteArray();
            }
            case "confirm" : {
                return routeSet.getChangeTime().toString().getBytes();
            }


            default : {
                return "Хмм, как эта команда сюда попала....".getBytes();
            }

        }


    }


}
