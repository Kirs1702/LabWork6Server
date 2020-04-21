package main.commands.specific;

import main.commands.Command;
import main.entity.Route;
import main.entity.RouteSet;

public class ShowCommand extends Command {


    public ShowCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {
        if (routeSet.size() == 0){
            System.out.println("Коллекция пуста.");
        }
        else {
            System.out.println("Список элементов коллекции:");
            for (Route route : routeSet) {
                System.out.println(route.toString());
            }
        }
   }
    @Override
    public String getDescription() {
        return "Вывести все элементы коллекции в строковом представлении.";
    }
}
