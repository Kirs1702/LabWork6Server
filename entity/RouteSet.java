package main.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RouteSet {
    private LocalDateTime initTime;
    private Set<Route> set;

    /**
     * Конструктор, при котором автоматически заполняется initTime (дата создания коллекции)
     */
    public RouteSet() {
        initTime = LocalDateTime.now();
        HashSet<Route> hashSet = new HashSet<Route>();
        set = Collections.synchronizedSet(hashSet);
    }

    public int size() {
        return set.size();
    }

    public void add(Route route) {
        set.add(route);
    }

    public void remove(Route route) {
        set.remove(route);
    }

    public void clear() {
        set.clear();
    }
    public boolean isEmpty() {
        return set.isEmpty();
    }

    public Set<Route> getSet() {
        return set;
    }

    @Override
    public String toString() {
        return  "Тип коллекции: HashSet\n" +
                "Дата инициализации: " + initTime.toString() + "\n" +
                "Количество элементов: " + size();
    }
}
