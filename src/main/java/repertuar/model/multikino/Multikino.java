package repertuar.model.multikino;

import repertuar.model.Chain;
import repertuar.model.Website;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by anotender on 07.12.15.
 */
public class Multikino extends Chain {

    public Multikino(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() {
        Website cinemasWebsite = new Website("https://multikino.pl/pl/nasze-kina");
        cinemasWebsite.loadContent(false);
        LinkedList<String> content = cinemasWebsite.getContent();

        boolean isReadingCinemas = false;

        for (String line : content) {
            if (line.contains("cinemas-list")) {
                isReadingCinemas = true;
            } else if (isReadingCinemas && line.contains("div")) {
                break;
            } else if (isReadingCinemas && line.contains("href")) {
                line = line.trim();
                String url = "https://multikino.pl" + line.substring(line.indexOf("href=") + "href=".length() + 1, line.indexOf(">") - 1);

                line = line.replaceAll("<.*?>", "");

                String city;
                String name = "Multikino";

                if (line.indexOf(" ") >= 0) {
                    city = line.substring(0, line.indexOf(" "));
                    name += (" " + line.substring(line.indexOf(" ") + 1));
                } else {
                    city = line;
                }

                cinemas.add(new MultikinoCinema(name, city, url));
            }
        }
    }
}
