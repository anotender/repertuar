package repertuar.model.cinemaCity;

import org.apache.commons.lang3.StringEscapeUtils;
import repertuar.model.Chain;
import repertuar.model.Website;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by mateu on 09.12.2015.
 */
public class CinemaCity extends Chain {

    public CinemaCity(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() {
        Website w = new Website("http://www.cinema-city.pl/cinemas");
        w.loadContent(false);

        for (String line : w.getContent()) {
            if (line.contains("cinema_name")) {
                line = line.trim();
                line = line.replaceFirst("<.*?>", "");

                String url = "http://www.cinema-city.pl/" + line.substring(line.indexOf("=\"") + 2, line.indexOf("\">"));
                String city = StringEscapeUtils.unescapeHtml4(line.replaceAll("<.*?>", ""));

                cinemas.add(new CinemaCityCinema(null, city, url));
            }
        }
    }
}
