package main.entity;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ServerWorker  implements Runnable {

    ConsoleReader reader;

    public ServerWorker(ConsoleReader reader) {
        this.reader = reader;
    }


    public void run() {
        while (true) {
            try {
                System.out.println(reader.readCommand());
            } catch (NoSuchElementException | IOException | XMLStreamException e) {
                System.out.println("Работа завершена.");
                System.exit(0);
            }
        }
    }
}