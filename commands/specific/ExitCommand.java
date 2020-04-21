package main.commands.specific;

import main.commands.Command;
import main.entity.RouteSet;

public class ExitCommand extends Command {
    public ExitCommand(RouteSet routeSet, String name) {
        super(routeSet, name);
    }

    @Override
    public void execute(String... args) {
        System.out.println("Работа завершена.");
        System.exit(0);

    }

    @Override
    public String getDescription() {
        return "Завершить работу программы без сохранения в файл.";
    }
}
