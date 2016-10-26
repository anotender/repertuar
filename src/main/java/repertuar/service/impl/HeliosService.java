package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.helios.HeliosCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.Website;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HeliosService implements ChainService {
    @Override
    public List<Cinema> getCinemas() throws IOException {
        return new Website("http://helios.pl")
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("div")
                .stream()
                .filter(e -> e.hasAttribute("class") && "list".equals(e.getAttribute("class")))
                .findFirst()
                .orElseThrow(IOException::new)
                .getElementsByTagName("li")
                .stream()
                .flatMap(e -> e.getElementsByTagName("a").stream())
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

    private Cinema prepareCinema(DomElement e) {
        String href = e.getAttribute("href");
        Integer id = Integer.parseInt(
                href.substring(
                        href.indexOf('/') + 1,
                        href.indexOf(',')
                )
        );

        String city = e
                .getElementsByTagName("strong")
                .stream()
                .findFirst()
                .get()
                .getTextContent();

        String name = e
                .getElementsByTagName("span")
                .stream()
                .findFirst()
                .get()
                .getTextContent();

        return new HeliosCinema(id, city + name.replaceFirst("Helios", ""), "http://helios.pl" + href);
    }
}
