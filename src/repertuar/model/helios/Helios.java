package repertuar.model.helios;

import repertuar.model.Chain;
import repertuar.model.Website;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by anotender on 07.12.15.
 */
public class Helios extends Chain {

    public Helios(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() {
        website.loadContent(false);
        LinkedList<String> content = website.getContent();

        for (String line : content) {
            if (line.contains("<strong class=\"city\">") && line.contains("<span class=\"name\">")) {
                line = line.trim();

                String url = "http://www.helios.pl" + line.substring(line.indexOf("\"") + 1, line.indexOf("\">"));
                String city = line.substring(line.indexOf("<strong class=\"city\">") + "<strong class=\"city\">".length(), line.indexOf("</strong>"));
                String name = line.substring(line.indexOf("<span class=\"name\">") + "<span class=\"name\">".length(), line.indexOf("</span>"));

                cinemas.add(new HeliosCinema(name, city, url));
            }
        }
    }
}
