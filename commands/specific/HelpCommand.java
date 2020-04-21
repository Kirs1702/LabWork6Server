package main.commands.specific;

import main.commands.Command;
import main.commands.CommandList;
import main.entity.RouteSet;

public class HelpCommand extends Command {

    CommandList comList;
    public HelpCommand(CommandList comList, RouteSet routeSet, String name) {
        super(routeSet, name);
        this.comList = comList;
    }

    @Override
    public void execute(String... args) {
        System.out.println("Команда и аргументы вводятся через пробел, первое слово - команда, последующие - аргументы;" + "\n" +
                "Каждая команда принимает свой набор ангументов. В случае несоответствия набора аргументов команда выполнена не будет.");
        System.out.println("Список доступных команд:");
        for(Command com : comList) {
            System.out.println(com.getName() + " - " + com.getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "Вывести справку по доступным командам.";
    }
}
