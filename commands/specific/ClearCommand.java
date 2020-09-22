package main.commands.specific;

import main.commands.Command;
import main.entity.DataBaseHandler;
import main.entity.RouteSet;

public class ClearCommand extends Command {
    DataBaseHandler dbh;
    public ClearCommand(RouteSet routeSet, String name, DataBaseHandler dbh) {
        super(routeSet, name);
        this.dbh = dbh;
    }

    @Override
    public String  execute(String user, String... args) {
        dbh.deleteAll();
        routeSet.clear();
        return  "Коллекция очищена.";
    }

    @Override
    public String getDescription() {
        return "Очистить коллекцию.";
    }
}
