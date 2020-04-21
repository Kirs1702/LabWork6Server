package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.Scanner;

public class UpdateCommand extends Command {

    public UpdateCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
    }

    @Override
    public void execute(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        for (Route route : routeSet) {
            if (route.getId() == Long.parseLong(args[0])) {


                System.out.println("Обновление значений маршрута \"" + route.getName() + "\":");
                ConsoleReader.readRouteValues(scanner, route);
                System.out.println("Значения успешно обновлены.");


                success = true;
                break;
            }
        }
        if (!success) {
            System.out.println("Маршрута с данным id отсутствует в коллекции.");
        }
    }

    @Override
    public String getDescription() {
        return "Обновить значение элемента коллекции, id которого равен заданному.";
    }
}
