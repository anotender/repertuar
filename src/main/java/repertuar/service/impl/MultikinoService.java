package repertuar.service.impl;

import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.HttpUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MultikinoService implements ChainService {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/index")
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::prepareCinema)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", cinemaID.toString());

        return HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/dates", params)
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::prepareSeanceDay)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", cinemaID.toString());
        params.put("from", df.format(date));

        return HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/seances", params)
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::prepareFilm)
                .collect(Collectors.toList());
    }

    private Cinema prepareCinema(Map m) {
        String name = (String) m.get("name");
        String website = "https://multikino.pl/pl/repertuar/" + m.get("slug");
        Integer cinemaID = Integer.parseInt((String) m.get("id"));
        Integer cityID = Integer.parseInt((String) m.get("city_id"));
        return new MultikinoCinema(cinemaID, name, website, cityID);
    }

    private SeanceDay prepareSeanceDay(Map m) {
        try {
            return new SeanceDay(
                    df.parse((String) m.get("beginning_date")),
                    Collections.emptyList()
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Seance prepareSeance(Map m, int eventID) {
        return new Seance(
                ((String) m.get("beginning_date")).substring(11, 16),
                "https://multikino.pl/kup-bilet2/" + eventID
        );
    }

    private Film prepareFilm(Map m) {
        List<Seance> seances = ((List<Map>) m.get("seances"))
                .stream()
                .map(s -> {
                    int eventID = Integer.parseInt((String) m.get("event_id"));
                    return prepareSeance(s, eventID);
                })
                .collect(Collectors.toList());

        return new Film(
                m.get("title") + " " + m.get("version_name"),
                "https://multikino.pl/pl/filmy/" + m.get("slug"),
                seances
        );
    }
}
