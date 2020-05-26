package main.commands.specific;

import main.commands.Command;
import main.entity.RouteSet;
import main.entity.RouteSetXmlParser;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class SaveCommand extends Command {
    String filePath;
    public SaveCommand(String filePath, RouteSet routeSet, String name) {
        super(routeSet, name);
        this.filePath = filePath;
    }

    @Override
    public String execute(String... args) throws IOException, XMLStreamException {
        RouteSetXmlParser.RouteSetToXml(routeSet, filePath);
        return "";
    }

    @Override
    public String getDescription() {
        return "Сохранить коллекцию в файл.";
    }
}
