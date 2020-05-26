package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class UpdateCommand extends Command {

    public UpdateCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
        setArgsMask(1, "[0-9]{1,10}");
    }

    @Override
    public String execute(String... args) {

        AtomicReference<String> result = new AtomicReference<>("");

        routeSet.forEach(route -> {
            if (route.getId() == Long.parseLong(args[0])) {

                ConsoleReader.readRouteValues(new Scanner(System.in), route);
                result.set("Значения успешно обновлены.");

            }
        });

        result.set("Маршрут с данным id отсутствует в коллекции.");

        return result.get();

    }

    @Override
    public String getDescription() {
        return "Обновить значение элемента коллекции по id.";
    }
}
