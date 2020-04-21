package main.commands.specific;

import main.commands.Command;
import main.entity.RouteSet;

public class InfoCommand extends Command {

    public InfoCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {
        System.out.println("информация о коллекции:");
        System.out.println(routeSet.toString());
    }

    @Override
    public String getDescription() {
        return "Вывести информацию о коллекции.";
    }
}
