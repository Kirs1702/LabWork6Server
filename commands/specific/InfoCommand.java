package main.commands.specific;

import main.commands.Command;
import main.entity.RouteSet;

import java.util.concurrent.atomic.AtomicReference;

public class InfoCommand extends Command {

    public InfoCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public String  execute(String user, String... args) {
        String result = "";
        result = result.concat("Информация о коллекции:\n");
        result = result.concat(routeSet.toString());

        return result;
    }

    @Override
    public String getDescription() {
        return "Вывести информацию о коллекции.";
    }
}
