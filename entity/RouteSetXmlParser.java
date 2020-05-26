package main.entity;


import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class RouteSetXmlParser {

    /**
     * Метод, проверяющий файл XML на соответствие XML schema
     * @param pathXml путь к XML
     * @param pathXsd путь к XML schema
     * @return true если соответствует, false если нет
     */
    public static boolean checkXML(String pathXml, String pathXsd) {
        /*pathXml = pathXml.replaceAll("\\\\", "/");
        pathXsd = pathXsd.replaceAll("\\\\", "/");
        if (pathXml.startsWith("C:")) {
            pathXml = pathXml.substring(2);
        }
        if (pathXsd.startsWith("C:")) {
            pathXsd = pathXsd.substring(2);
        }*/
        try {
            File xml = new File(pathXml);
            File xsd = new File(pathXsd);

            if (!xml.exists()) {
                System.out.println("Не существует XML файл " + pathXml);
            }
            else if (xml.exists() && !xml.canRead()) {
                System.out.println("Недоступен к чтению XML файл " + pathXml);
            }

            if (!xsd.exists()) {
                System.out.println("Не существует XSD файл " + pathXsd);
            }
            else if (xsd.exists() && !xsd.canRead()) {
                System.out.println("Недоступен к чтению XSD файл " + pathXsd);
            }

            if (!xml.exists() || !xsd.exists() || !xml.canRead() || !xsd.canRead()) {
                return false;
            }

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new InputStreamReader(new FileInputStream(new File(pathXsd))))); // !!!
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new InputStreamReader(new FileInputStream(new File(pathXml))))); // !!!
            return true;
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    /**
     * Считывает коллекцию типа RouteSet из файла XML
     * @param pathXml путь к XML файлу
     * @param pathXsd путь к XML schema файлу
     * @param routeSet коллекция, в которую добавляются элементы
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException исключение ввода-вывода
     */
    public static void XmlToRouteSet(String pathXml, String pathXsd, RouteSet routeSet) throws ParserConfigurationException, SAXException, IOException {
        if (checkXML(pathXml, pathXsd)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RouteSetXmlHandler handler = new RouteSetXmlHandler(routeSet);
            parser.parse(new File(pathXml), handler);
        }
        else {
            System.out.println("Не удалось считать коллекцию из файла " + pathXml);
        }
    }

    /**
     * Записывает коллекцию типа RouteSet в файл XML
     * @param routeSet коллекция, элементы которой сохраняются
     * @param pathXml путь к XML файлу
     * @throws XMLStreamException
     */
    public static void RouteSetToXml(RouteSet routeSet, String pathXml) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = null;
        File xmlFile = new File(pathXml);               // do try catch

        if (xmlFile.exists() && !(new File(pathXml).canWrite())) {
            System.out.println("Файл " + pathXml + " недоступен к записи.");
            return;
        }

        writer = output.createXMLStreamWriter(new FileOutputStream(new File(pathXml))); // !!!


        writer.writeStartDocument("1.0");
        writer.writeStartElement("routeSet");

        for (Route route : routeSet) {

            writer.writeStartElement("route");
            writer.writeAttribute("id", String.valueOf(route.getId()));
            writer.writeAttribute("name", route.getName());
            writer.writeAttribute("distance", String.valueOf(route.getDistance()));

            writer.writeStartElement("coordinates");
            writer.writeAttribute("x", String.valueOf(route.getCoordinates().getX()));
            writer.writeAttribute("y", String.valueOf(route.getCoordinates().getY()));
            writer.writeEndElement();

            writer.writeStartElement("creationDate");
            writer.writeAttribute("year", String.valueOf(route.getCreationDate().getYear()));
            writer.writeAttribute("month", String.valueOf(route.getCreationDate().getMonthValue()));
            writer.writeAttribute("day", String.valueOf(route.getCreationDate().getDayOfMonth()));
            writer.writeAttribute("hours", String.valueOf(route.getCreationDate().getHour()));
            writer.writeAttribute("minutes", String.valueOf(route.getCreationDate().getMinute()));
            writer.writeAttribute("seconds", String.valueOf(route.getCreationDate().getMinute()));
            writer.writeEndElement();

            if (route.getFrom() != null){
                writer.writeStartElement("locationFrom");
                if(route.getFrom().getName() != null) {
                    writer.writeAttribute("name", route.getFrom().getName());
                }
                writer.writeAttribute("x", String.valueOf(route.getFrom().getX()));
                writer.writeAttribute("y", String.valueOf(route.getFrom().getY()));
                writer.writeEndElement();
            }

            writer.writeStartElement("locationTo");
            if(route.getTo().getName() != null) {
                writer.writeAttribute("name", route.getTo().getName());
            }
            writer.writeAttribute("x", String.valueOf(route.getTo().getX()));
            writer.writeAttribute("y", String.valueOf(route.getTo().getY()));
            writer.writeEndElement();

            writer.writeEndElement();
        }


        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        System.out.println("Данные были сохранены в файл " + pathXml + ".");
    }
}

