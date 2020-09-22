package main.commands.specific;

import main.commands.Command;
import main.entity.RouteSet;


public class SaveCommand extends Command {
    String filePath;
    public SaveCommand(String filePath, RouteSet routeSet, String name) {
        super(routeSet, name);
        this.filePath = filePath;
    }

    @Override
    public String execute(String user, String... args){

        return "";
    }

    @Override
    public String getDescription() {
        return "Сохранить коллекцию в файл.";
    }
}
