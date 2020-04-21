package main.commands;

import main.entity.RouteSet;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.regex.Pattern;

public abstract class Command {
    protected String name;
    protected RouteSet routeSet;
    private String[] argsMask;

    /**
     * Конструктор
     * @param routeSet коллекция, с  которой работает команда
     * @param name имя команды
     */
    public Command(RouteSet routeSet, String name){
        this.name = name;
        this.routeSet = routeSet;
        argsMask = null;

    }

    /**
     * Метод, выполняя которыый в конструкторе конкретных комманд
     * @param argsNun количество аргументов
     * @param masks маски для каждого аргумента
     */
    protected void setArgsMask(int argsNun, String... masks){
        argsMask = new String[argsNun];
        System.arraycopy(masks, 0, argsMask, 0, argsNun);

    }


    /**
     * Метод, проверяющий совпадение аргументов с треуемыми
     * @param args аргументы на проверку
     * @return true если соответствует, иначе false
     */
    public boolean matchesArgs(String... args) {

        if (args == null && getArgsMask() == null) {
            return true;
        }
        else if (args == null && getArgsMask() != null) {
            return false;
        }
        else if (args != null && getArgsMask() == null) {
            return false;
        }

        else if (getArgsMask().length != args.length) {
            return false;
        }else {
            for (int i = 0; i < getArgsMask().length; i++) {
                if (!Pattern.matches(getArgsMask()[i], args[i])) {
                    return false;
                }
            }
        }
        return  false;
    }

    /**
     * Метод выполнения команды с аргументами
     * @param args аргументы
     * @throws IOException
     * @throws XMLStreamException
     */
    public abstract void execute(String... args) throws IOException, XMLStreamException;

    public String getName() {
        return name;
    }

    public String[] getArgsMask() {
        return argsMask;
    }

    /**
     * Абстрактный метод, возвращающий описание команды
     * @return описание команды
     */
    public abstract String getDescription();

}
