package main.commands;


import main.entity.ConsoleReader;
import main.entity.DataBaseHandler;
import main.entity.RouteSet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class CommandList extends ArrayList<Command>{
    /**
     * Стандартная инициализация команды
     * @param routeSet обрабатываемая коллекция
     * @param name имя команды, по которому она вызывается
     * @param clazz класс, в котором расположена команда
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void initCommand(RouteSet routeSet, String name, Class<? extends Command> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        add(clazz.getConstructor(RouteSet.class, String.class).newInstance(routeSet, name));
    }

    /**
     * инициализация команды, требующей доступа к списку комманд
     * @param comList список команд
     * @param routeSet обрабатываемая коллекция
     * @param name имя команды, по которому она вызывается
     * @param clazz класс, в котором расположена команда
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void initCommand(CommandList comList, RouteSet routeSet, String name, Class<? extends Command> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        add(clazz.getConstructor(CommandList.class, RouteSet.class, String.class).newInstance(comList, routeSet, name));
    }

    /**
     * инициализация команды, влияющей на работу ConsoleReader
     * @param reader ConsoleReader
     * @param routeSet обрабатываемая коллекция
     * @param name имя команды, по которому она вызывается
     * @param clazz класс, в котором расположена команда
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void initCommand(ConsoleReader reader, RouteSet routeSet, String name, Class<? extends Command> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        add(clazz.getConstructor(ConsoleReader.class, RouteSet.class, String.class).newInstance(reader, routeSet, name));
    }

    /**
     * инициализация команды, работающей с XML файлом
     * @param pathXml путь к XML файлу
     * @param routeSet обрабатываемая коллекция
     * @param name имя команды, по которому она вызывается
     * @param clazz класс, в котором расположена команда
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void initCommand(String pathXml, RouteSet routeSet, String name, Class<? extends Command> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        add(clazz.getConstructor(String.class, RouteSet.class, String.class).newInstance(pathXml, routeSet, name));
    }

    public void initCommand(RouteSet routeSet, String name, DataBaseHandler dbh, Class<? extends Command> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        add(clazz.getConstructor(RouteSet.class, String.class, DataBaseHandler.class).newInstance(routeSet, name, dbh));
    }


}
