package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import org.apache.commons.lang3.time.DateUtils;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.Website;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CinemaCityService implements ChainService {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

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
        List<SeanceDay> seanceDays = new LinkedList<>();

        for (int i = 0; i < 7; i++) {
            seanceDays.add(
                    new SeanceDay(
                            DateUtils.addDays(new Date(), i),
                            Collections.emptyList()
                    )
            );
        }

        return seanceDays;
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
//        new Website("https://www.cinema-city.pl/scheduleInfo?locationId=" + cinemaID + "&date=" + df.format(date))
//                .loadPageWithJavaScriptDisabled()
//                .getElementsByTagName("tr")
//                .stream()
//                .filter(e -> e.hasAttribute("class") && ("odd".equals(e.getAttribute("class")) || "even".equals(e.getAttribute("class"))))
//                .flatMap(e -> e.getElementsByTagName("td").stream())
//                .filter(e -> e.hasAttribute("class") && "featureName".equals(e.getAttribute("class")))
//                .map(DomNode::getTextContent)
//                .forEach(System.out::println);

        return Collections.emptyList();
    }

    private Cinema prepareCinema(DomElement e) {
        return new CinemaCityCinema(Integer.parseInt(e.getAttribute("data-site_id")), e.getTextContent(), "https://cinema-city/");
    }
}
