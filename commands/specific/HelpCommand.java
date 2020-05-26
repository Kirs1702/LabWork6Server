package main.commands.specific;

import main.commands.Command;
import main.commands.CommandList;
import main.entity.RouteSet;

import java.util.concurrent.atomic.AtomicReference;

public class HelpCommand extends Command {

    CommandList comList;
    public HelpCommand(CommandList comList, RouteSet routeSet, String name) {
        super(routeSet, name);
        this.comList = comList;
    }

    @Override
    public String execute(String... args) {
        AtomicReference<String> ans = new AtomicReference<>("");
        ans.set(ans + "Команда и аргументы вводятся через пробел, первое слово - команда, последующие - аргументы;\n" +
                "Каждая команда принимает свой набор ангументов. В случае несоответствия набора аргументов команда выполнена не будет.\nСписок доступных команд:\n");
        comList.forEach(com -> ans.set(ans + com.getName() + " - " + com.getDescription() + "\n"));
        return  ans.get();
    }

    @Override
    public String getDescription() {
        return "Вывести справку по доступным командам.";
    }
}
