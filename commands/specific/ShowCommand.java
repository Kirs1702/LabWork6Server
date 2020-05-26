package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

import java.util.concurrent.atomic.AtomicReference;

public class ShowCommand extends Command {


    public ShowCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public String execute(String... args) {


        AtomicReference<String> result = new AtomicReference<>("");
        if (routeSet.size() == 0){
            return "Коллекция пуста.";
        }
        else {
            result.set(result + "Список элементов коллекции:\n");
            routeSet.forEach(route -> result.set(result + route.toString() + "\n"));
        }
        return  result.get();
   }
    @Override
    public String getDescription() {
        return "Вывести все элементы коллекции в строковом представлении.";
    }
}
