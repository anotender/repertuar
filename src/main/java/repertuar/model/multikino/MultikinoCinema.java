package repertuar.model.multikino;

import javafx.collections.FXCollections;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.utils.HttpUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultikinoCinema extends Cinema {

    private Integer cinemaID;
    private Integer cityID;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MultikinoCinema(String name, String city, String url, Integer cinemaID, Integer cityID) {
        super(name, city, url);
        this.cinemaID = cinemaID;
        this.cityID = cityID;
    }

    @Override
    public void loadDays() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", cinemaID.toString());

        HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/dates", params)
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::prepareSeanceDay)
                .forEach(days::add);
    }

    @Override
    public void loadFilms(int day, String date) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", cinemaID.toString());
        params.put("from", date);

        List<Film> films = HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/seances", params)
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::prepareFilm)
                .collect(Collectors.toList());

        days.get(day).setFilms(FXCollections.observableList(films));
    }

    private SeanceDay prepareSeanceDay(Map m) {
        return new SeanceDay(
                ((String) m.get("beginning_date")).substring(0, 10),
                FXCollections.observableList(new LinkedList<>())
        );
    }

    private Seance prepareSeance(Map m) {
        return new Seance(
                ((String) m.get("beginning_date")).substring(11, 16),
                "http://onet.pl"
        );
    }

    private Film prepareFilm(Map m) {
        List<Seance> seances = ((List<Map>) m.get("seances"))
                .stream()
                .map(this::prepareSeance)
                .collect(Collectors.toList());

        return new Film(
                m.get("title") + " " + m.get("version_name"),
                "https://multikino.pl/pl/filmy/" + m.get("slug"),
                seances
        );
    }


}
