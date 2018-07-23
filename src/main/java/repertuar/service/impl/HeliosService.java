package repertuar.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.helios.HeliosCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HeliosService implements ChainService {

    private final String baseUrl = "http://helios.pl/";

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return Jsoup.connect(baseUrl)
                .get()
                .body()
                .select("section.cinema-list")
                .select("div.list")
                .select("a[href]")
                .stream()
                .map(this::prepareCinema)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) throws IOException {
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        return Jsoup.connect(baseUrl + cinemaID + "/Repertuar/index/dzien/" + daysDifference(new Date(), date))
                .get()
                .body()
                .select("li.seance")
                .stream()
                .map(this::prepareFilm)
                .collect(Collectors.toList());
    }

    private Cinema prepareCinema(Element e) {
        String url = baseUrl + e.attr("href");
        Integer id = Integer.parseInt(e.attr("href").substring(1, e.attr("href").indexOf(",")));
        String name = e.select("strong").text() + e.select("span").text().replace("Helios", "");
        return new HeliosCinema(id, name, url);
    }

    private Film prepareFilm(Element e) {
        String title = e.select("h2.movie-title").text();
        String url = baseUrl + e.select("a.movie-link").attr("href");
        List<Seance> seances = e
                .select("a.hour-link.fancybox-reservation")
                .stream()
                .map(this::prepareSeance)
                .collect(Collectors.toList());

        return new Film("", title, url, seances);
    }

    private Seance prepareSeance(Element e) {
        String hour = e.text();
        String seanceUrl = baseUrl + e.attr("href");
        return new Seance(hour, seanceUrl);
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
