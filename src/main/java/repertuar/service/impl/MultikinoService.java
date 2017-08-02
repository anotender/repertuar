package repertuar.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;
import repertuar.utils.Website;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultikinoService implements ChainService {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return new Website("https://multikino.pl/nasze-kina")
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("li")
                .stream()
                .filter(e -> e.hasAttribute("class"))
                .filter(e -> e.getAttribute("class").equals("ml-columns__item"))
                .map(DomElement::getFirstElementChild)
                .map(e -> new MultikinoCinema(e.getTextContent(), "https://multikino.pl" + e.getAttribute("href")))
                .map(this::fetchCinemaId)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaId) throws IOException {
        return RepertoireUtils.getSeanceDays(5);
    }

    @Override
    public List<Film> getFilms(Integer cinemaId, Date date) throws IOException {
        return new JSONObject(HttpUtils
                .sendGet("https://multikino.pl/data/filmswithshowings/" + cinemaId))
                .getJSONArray("films")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .filter(this::isNowPlaying)
                .filter(m -> matchesDate(m, date))
                .map(m -> prepareFilm(m, cinemaId, date))
                .collect(Collectors.toList());
    }

    private MultikinoCinema fetchCinemaId(MultikinoCinema c) {
        try {
            String stringId = new Website(c.getUrl() + "/repertuar")
                    .loadPageWithJavaScriptDisabled()
                    .getBody()
                    .getAttribute("data-selected-locationid");

            c.setId(Integer.parseInt(stringId));
        } catch (Exception ignored) {
        }
        return c;
    }

    private boolean matchesDate(Map m, Date date) {
        return ((List<Map>) m.get("showings"))
                .stream()
                .map(s -> {
                    try {
                        return df.parse(s.get("date_time").toString());
                    } catch (ParseException e) {
                        return new Date();
                    }
                })
                .anyMatch(d -> DateUtils.isSameDay(date, d));
    }

    private boolean isNowPlaying(Map m) {
        boolean hasSeances = (int) m.get("original_s_count") > 0;
        boolean comingSoon = (boolean) m.get("coming_soon");
        boolean announcement = (boolean) m.get("announcement");
        return hasSeances && !comingSoon && !announcement;
    }

    private Seance prepareSeance(Map m, int filmId, int cinemaId) {
        String versionId = m.get("version_id").toString();
        String sessionId = m.get("session_id").toString();
        return new Seance(
                m.get("time").toString(),
                "https://multikino.pl/kupbilet/" + filmId + "/" + cinemaId + "/" + versionId + "/" + sessionId + "/wybierz-miejsce"
        );
    }

    private Film prepareFilm(Map m, int cinemaId, Date date) {
        int filmId = Integer.parseInt(m.get("id").toString());

        List<Seance> seances = ((List<Map>) m.get("showings"))
                .stream()
                .filter(s -> {
                    try {
                        return DateUtils.isSameDay(date, df.parse(s.get("date_time").toString()));
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .flatMap(s -> ((List<Map>) s.get("times")).stream())
                .map(s -> prepareSeance(s, filmId, cinemaId))
                .collect(Collectors.toList());

        return new Film(
                m.get("title").toString(),
                "https://multikino.pl" + m.get("filmlink").toString(),
                seances
        );
    }
}
