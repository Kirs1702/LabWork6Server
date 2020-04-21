package main.entity;

public class EnvironmentGetter {

    /**
     * Возвращает значение переменной окружения
     * @param name имя переменной окружения
     * @return возвращает значение переменной окружения
     */
    public static String getEnv(String name)
    {
        String path = System.getenv(name);
        if (path == null || path.equals("")) {
            System.out.println("Не удалось считать путь из переменной окружения " + name + ".");
            return null;
        } else {
            return path;
        }

    }
}
