package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import org.apache.commons.lang3.time.DateUtils;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.helios.HeliosCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.RepertoireUtils;
import repertuar.utils.Website;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        return new Website("http://helios.pl/" + cinemaID + "/Repertuar/index/dzien/" + daysDifference(new Date(), date))
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("li")
                .stream()
                .filter(e -> e.hasAttribute("class") && "seance".equals(e.getAttribute("class")))
                .map(this::prepareFilm)
                .collect(Collectors.toList());
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

    private Film prepareFilm(DomElement element) {
        return new Film(
                extractTitle(element),
                "http://helios.pl" + element.getAttribute("href"),
                extractSeances(element)
        );
    }

    private String extractTitle(DomElement element) {
        return element
                .getElementsByTagName("a")
                .stream()
                .filter(e -> e.hasAttribute("class") && "movie-link".equals(e.getAttribute("class")))
                .flatMap(e -> StreamSupport.stream(e.getChildren().spliterator(), false))
                .map(DomNode::getTextContent)
                .map(String::trim)
                .map(s -> {
                    if (s.startsWith("/")) {
                        s = s.replace('/', ',');
                    }
                    return s;
                })
                .collect(Collectors.joining());
    }

    private List<Seance> extractSeances(DomElement element) {
        return element
                .getElementsByTagName("a")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").contains("hour-link"))
                .map(e -> new Seance(
                        e.getTextContent(),
                        "http://helios.pl" + e.getAttribute("href")
                ))
                .collect(Collectors.toList());
    }

    private int daysDifference(Date d1, Date d2) {
        Date earlierDate = d1.before(d2) ? d1 : d2;
        Date laterDate = d1.before(d2) ? d2 : d1;

        int diff = 0;

        while (!DateUtils.isSameDay(earlierDate, laterDate)) {
            earlierDate = DateUtils.addDays(earlierDate, 1);
            diff++;
        }

        return diff;
    }

}
