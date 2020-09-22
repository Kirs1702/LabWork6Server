package main.commands.specific;

import main.commands.Command;
import main.entity.ConsoleReader;
import main.entity.RouteSet;

public class HistoryCommand extends Command {
    private ConsoleReader reader;
    public HistoryCommand(ConsoleReader reader, RouteSet routeSet, String name) {
        super(routeSet, name);
        this.reader = reader;
    }

    @Override
    public String  execute(String user, String... args) {
        reader.showHistory();
        return "";
    }

    @Override
    public String getDescription() {
        return "Вывести последние 10 введённых команд.";
    }
}
