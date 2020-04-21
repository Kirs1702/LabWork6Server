package main.entity;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RouteSetXmlHandler extends DefaultHandler {

    private RouteSet routeSet;
    private Route route = new Route();


    /**
     * Конструктор, в котором передаётся коллекция
     * @param routeSet коллекция типа RouteSet
     */
    public RouteSetXmlHandler(RouteSet routeSet) {
        this.routeSet = routeSet;
    }


    /**
     * Обработка начала элемента
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equals("route")){
            route.setFrom(null);
            route.setId(Long.parseLong(attributes.getValue("id")));
            route.setName(attributes.getValue("name"));
            route.setDistance(Integer.parseInt(attributes.getValue("distance")));
        }

        if (qName.equals("coordinates")){
            route.setCoordinates(new Coordinates(Float.parseFloat(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y"))));
        }

        if (qName.equals("creationDate")){
             route.setCreationDate(Integer.parseInt(attributes.getValue("year")), Integer.parseInt(attributes.getValue("month")),
                     Integer.parseInt(attributes.getValue("day")), Integer.parseInt(attributes.getValue("hours")),
                     Integer.parseInt(attributes.getValue("minutes")), Integer.parseInt(attributes.getValue("seconds")));
        }

        if (qName.equals("locationFrom")){
            route.setFrom(new Location(Integer.parseInt(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y")),
                    attributes.getValue("name")));
        }
        if (qName.equals("locationTo")){
            route.setTo(new Location(Integer.parseInt(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y")),
                    attributes.getValue("name")));
        }

    }

    /**
     * Обработка конца элемента
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("route")){
            boolean success = true;
            for (Route rout : routeSet){
                if (rout.getId() == route.getId()) {
                    success = false;
                }
            }

            if (success) {
                routeSet.add(route);
                System.out.println("Успешно добавлен маршрут с именем " + route.getName() + ".");
            }
            else {
                System.err.println("Маршрут с именем " + route.getName() + " не был добавлен. В коллекции имеется элемент с идентичным id.");
            }



            route = new Route();
        }
    }
}
