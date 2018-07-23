package repertuar.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultikinoService implements ChainService {

    private final String baseUrl = "https://multikino.pl/";
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return Jsoup
                .connect(baseUrl + "nasze-kina")
                .get()
                .body()
                .select("li.ml-columns__item")
                .select("a")
                .stream()
                .map(e -> new MultikinoCinema(e.text(), baseUrl + e.attr("href")))
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
                .sendGet(baseUrl + "data/filmswithshowings/" + cinemaId))
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
            String stringId = Jsoup
                    .connect(c.getUrl() + "/repertuar")
                    .get()
                    .body()
                    .attr("data-selected-locationid");

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
        String seanceUrl = baseUrl + "kupbilet/" + filmId + "/" + cinemaId + "/" + versionId + "/" + sessionId + "/wybierz-miejsce";
        return new Seance(m.get("time").toString(), seanceUrl);
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

        return new Film("", m.get("title").toString(), baseUrl + m.get("filmlink").toString(), seances);
    }
}
