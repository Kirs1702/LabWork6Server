package main.entity;

import sun.nio.cs.ext.MSISO2022JP;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataBaseHandler {
    String user;
    String password;
    String url;

    public DataBaseHandler(String url, String user, String password) {
        this.url = url;
        this.password = password;
        this.user = user;
    }


    /**
     * Сформировать RouteSet из всех имеющихся в базе routes
     *
     * @return RouteSet
     */
    public RouteSet selectAllRoutes() {
        try {
            Class.forName("org.postgresql.Driver");

            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM routes");
            RouteSet routeSet = new RouteSet();
            while (rs.next()) {
                Route route = new Route(rs.getLong("id"), rs.getString("name"),
                        new Coordinates(rs.getFloat("coordinatesx"), rs.getInt("coordinatesy")),
                        LocalDateTime.parse(rs.getString("datetime"), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        new Location(rs.getInt("locationfromx"), rs.getInt("locationfromy"), rs.getString("locationfromname")),
                        new Location(rs.getInt("locationtox"), rs.getInt("locationtoy"), rs.getString("locationtoname")),
                        rs.getInt("distance"));
                route.setUser(rs.getString("username"));
                routeSet.add(route);

            }
            stmt.close();
            rs.close();
            return routeSet;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Добавить маршрут в базу данных, вернуть его id
     *
     * @param route маршрут для добавления
     * @return генерируемый БД id маршрута
     */
    public long insertRouteReturnId(Route route) {
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO routes (username, name, coordinatesx, coordinatesy, datetime, " +
                    "locationfromname, locationfromx, locationfromy, locationtoname, locationtox, locationtoy, distance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, route.getUser());
            prepStmt.setString(2, route.getName());
            prepStmt.setFloat(3, route.getCoordinates().getX());
            prepStmt.setInt(4, route.getCoordinates().getY());
            prepStmt.setString(5, route.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            try {
                prepStmt.setString(6, route.getFrom().getName());
                prepStmt.setInt(7, route.getFrom().getX());
                prepStmt.setInt(8, route.getFrom().getY());
            } catch (NullPointerException ex) {
                prepStmt.setNull(6, Types.VARCHAR);
                prepStmt.setNull(7, Types.INTEGER);
                prepStmt.setNull(8, Types.INTEGER);
            }
            prepStmt.setString(9, route.getTo().getName());
            prepStmt.setInt(10, route.getTo().getX());
            prepStmt.setInt(11, route.getTo().getY());
            prepStmt.setInt(12, route.getDistance());
            int res = prepStmt.executeUpdate();
            if (res == 0) return 0;
            prepStmt.close();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM routes");
            rs.next();
            long id = rs.getLong("max");
            rs.close();
            stmt.close();
            return id;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }


    }

    /**
     * добавить маршрут в базу данных
     *
     * @param route маршрут
     * @param id    id маршрута
     * @return 1 - если маршрут добавлен, 0 - если нет
     */
    public int insertRoute(Route route, long id) {
        int res;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO routes (username, name, coordinatesx, coordinatesy, datetime, " +
                    "locationfromname, locationfromx, locationfromy, locationtoname, locationtox, locationtoy, distance, id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, route.getUser());
            prepStmt.setString(2, route.getName());
            prepStmt.setFloat(3, route.getCoordinates().getX());
            prepStmt.setInt(4, route.getCoordinates().getY());
            prepStmt.setString(5, route.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            try {
                prepStmt.setString(6, route.getFrom().getName());
                prepStmt.setInt(7, route.getFrom().getX());
                prepStmt.setInt(8, route.getFrom().getY());
            } catch (NullPointerException ex) {
                prepStmt.setNull(6, Types.VARCHAR);
                prepStmt.setNull(7, Types.INTEGER);
                prepStmt.setNull(8, Types.INTEGER);
            }
            prepStmt.setString(9, route.getTo().getName());
            prepStmt.setInt(10, route.getTo().getX());
            prepStmt.setInt(11, route.getTo().getY());
            prepStmt.setInt(12, route.getDistance());
            prepStmt.setLong(13, id);
            res = prepStmt.executeUpdate();
            prepStmt.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return res;
    }


    public int setRouteById(Route route, long id) {
        int res;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("UPDATE routes SET username = ?, name = ?, coordinatesx = ?, coordinatesy = ?, datetime = ?, " +
                    "locationfromname = ?, locationfromx = ?, locationfromy = ?, locationtoname = ?, locationtox = ?, locationtoy = ?, distance = ? WHERE id = ? ");
            prepStmt.setString(1, route.getUser());
            prepStmt.setString(2, route.getName());
            prepStmt.setFloat(3, route.getCoordinates().getX());
            prepStmt.setInt(4, route.getCoordinates().getY());
            prepStmt.setString(5, route.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            try {
                prepStmt.setString(6, route.getFrom().getName());
                prepStmt.setInt(7, route.getFrom().getX());
                prepStmt.setInt(8, route.getFrom().getY());
            } catch (NullPointerException ex) {
                prepStmt.setNull(6, Types.VARCHAR);
                prepStmt.setNull(7, Types.INTEGER);
                prepStmt.setNull(8, Types.INTEGER);
            }
            prepStmt.setString(9, route.getTo().getName());
            prepStmt.setInt(10, route.getTo().getX());
            prepStmt.setInt(11, route.getTo().getY());
            prepStmt.setInt(12, route.getDistance());
            prepStmt.setLong(13, id);
            res = prepStmt.executeUpdate();
            prepStmt.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return res;
    }


    public int deleteRouteById(long id) {
        int res;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("DELETE FROM routes WHERE id = ?");
            prepStmt.setLong(1, id);
            res = prepStmt.executeUpdate();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return res;
    }


    public int deleteRouteByDistance(int distance) {
        int res;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("DELETE FROM routes WHERE distance = ?");
            prepStmt.setLong(1, distance);
            res = prepStmt.executeUpdate();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return res;
    }


    public int deleteAll() {
        int res;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("DELETE FROM routes");
            res = prepStmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return res;
    }


    public boolean register(String username, String pass) {
        ResultSet rs;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM USERS");
            rs = prepStmt.executeQuery();
            boolean wrong = false;
            while (rs.next()) {
                String name = rs.getString(1);
                if (name.equals(username)) {
                    wrong = true;
                    break;
                }
            }
            if (wrong) return false;
            else {
                prepStmt = con.prepareStatement("INSERT INTO users (username, password, salt) VALUES (?, ?, ?)");
                prepStmt.setString(1, username);
                String salt = HashMachine.generateSalt();
                prepStmt.setString(3, salt);
                String hashedPassword = HashMachine.addSaltPepperAndHash(pass, salt, "lS6FB0XEibfunBgssf2");
                prepStmt.setString(2, hashedPassword);
                int res = prepStmt.executeUpdate();
                if (res <= 0) return false;
                else return true;

            }


        } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean login(String username, String pass) {
        ResultSet rs;
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM USERS");
            rs = prepStmt.executeQuery();
            boolean wrong = true;
            while (rs.next()) {
                String name = rs.getString(1);
                if (name.equals(username)) {
                    wrong = false;
                    break;
                }
            }


            if (wrong) return false;
            else {
                String salt = rs.getString(3);
                String hashedPassword = HashMachine.addSaltPepperAndHash(pass, salt, "lS6FB0XEibfunBgssf2");
                if (hashedPassword.equals(rs.getString(2))) {
                    return true;
                }
            }


        } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
