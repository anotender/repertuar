package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import org.apache.commons.lang3.time.DateUtils;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.RepertoireUtils;
import repertuar.utils.Website;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CinemaCityService implements ChainService {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Map<String, String> map = new HashMap<>();

    public CinemaCityService() {
        map.put("DUB", "Dubbing");
        map.put("NAP", "Napisy");
    }

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return new Website("http://www.cinema-city.pl/cinemas")
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("a")
                .stream()
                .filter(e -> e.hasAttribute("data-site_id"))
                .map(this::prepareCinema)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) throws IOException {
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        return new Website("https://www.cinema-city.pl/scheduleInfo?locationId=" + cinemaID + "&date=" + df.format(date))
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("tr")
                .stream()
                .filter(e -> e.hasAttribute("class") && ("odd".equals(e.getAttribute("class")) || "even".equals(e.getAttribute("class"))))
                .map(this::prepareFilm)
                .collect(Collectors.toList());
    }

    private Cinema prepareCinema(DomElement e) {
        return new CinemaCityCinema(Integer.parseInt(e.getAttribute("data-site_id")), e.getTextContent(), "https://cinema-city/");
    }

    private Film prepareFilm(DomElement element) {
        String title = element
                .getElementsByTagName("td")
                .stream()
                .filter(e -> e.hasAttribute("class") && "featureName".equals(e.getAttribute("class")))
                .findFirst()
                .get()
                .getTextContent();

        title += element
                .getElementsByTagName("td")
                .stream()
                .map(DomNode::getTextContent)
                .map(String::trim)
                .filter(s -> map.containsKey(s))
                .map(s -> ", " + map.get(s))
                .findFirst()
                .orElse("");

        List<Seance> seances = element
                .getElementsByTagName("td")
                .stream()
                .flatMap(e -> e.getElementsByTagName("a").stream())
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").contains("presentationLink"))
                .map(e -> new Seance(e.getTextContent().trim(), "http://www.cinema-city.pl/cinemas"))
                .collect(Collectors.toList());

        return new Film(title, "http://www.cinema-city.pl/cinemas", seances);
    }
}
