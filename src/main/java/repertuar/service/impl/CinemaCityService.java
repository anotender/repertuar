package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.Website;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CinemaCityService implements ChainService {
    @Override
    public List<Cinema> getCinemas() throws IOException {
        return new Website("http://www.cinema-city.pl/cinemas")
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("td")
                .stream()
                .filter(e -> e.hasAttribute("class") && "cinema_info".equals(e.getAttribute("class")))
                .flatMap(e -> e.getElementsByTagName("div").stream())
                .filter(e -> e.hasAttribute("class") && "cinema_name".equals(e.getAttribute("class")))
                .map(this::prepareCinema)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) throws IOException {
        return null;
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        return null;
    }

    private Cinema prepareCinema(HtmlElement e) {
        try {
            String url = "https://cinema-city/" + e
                    .getElementsByTagName("a")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ParseException("No 'a' tag element", 0))
                    .getAttribute("href");
            return new CinemaCityCinema(0, e.getTextContent(), url);
        } catch (ParseException exc) {
            exc.printStackTrace();
        }
        return null;
    }
}
