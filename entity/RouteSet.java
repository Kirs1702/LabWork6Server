package main.entity;

import java.time.LocalDateTime;
import java.util.HashSet;

public class RouteSet extends HashSet<Route> {
    private LocalDateTime initTime;

    /**
     * Конструктор, при котором автоматически заполняется initTime (дата создания коллекции)
     */
    public RouteSet() {
        initTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return  "Тип коллекции: HashSet\n" +
                "Дата инициализации: " + initTime.toString() + "\n" +
                "Количество элементов: " + size();
    }
}
