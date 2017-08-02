package repertuar.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CinemaCityService implements ChainService {

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public List<Cinema> getCinemas() throws IOException {
        return new JSONArray(HttpUtils
                .sendGet("https://www.cinema-city.pl/pgm-sites"))
                .toList()
                .stream()
                .map(Map.class::cast)
                .map(this::prepareCinema)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDay> getSeanceDays(Integer cinemaID) throws IOException {
        return RepertoireUtils.getSeanceDays(7);
    }

    @Override
    public List<Film> getFilms(Integer cinemaID, Date date) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("si", cinemaID.toString());

        return new JSONArray(HttpUtils
                .sendGet("https://www.cinema-city.pl/pgm-site", params))
                .toList()
                .stream()
                .map(Map.class::cast)
                .map(JSONObject::new)
                .filter(o -> matchesDate(o, date))
                .map(o -> prepareFilm(o, date))
                .collect(Collectors.toList());
    }

    private CinemaCityCinema prepareCinema(Map m) {
        Integer id = Integer.parseInt(m.get("id").toString());
        String name = m.get("n").toString();
        String url = "https://www.cinema-city.pl" + m.get("url").toString();
        return new CinemaCityCinema(id, name, url);
    }

    private Film prepareFilm(JSONObject json, Date date) {
        List<Seance> seances = json
                .getJSONArray("BD")
                .toList()
                .stream()
                .map(Map.class::cast)
                .map(JSONObject::new)
                .filter(sd -> sd.getString("date").equals(df.format(date)))
                .flatMap(sd -> sd
                        .getJSONArray("P")
                        .toList()
                        .stream()
                        .map(Map.class::cast)
                        .map(JSONObject::new)
                )
                .map(s -> new Seance(s.getString("time")))
                .collect(Collectors.toList());

        return new Film(json.getString("n"), seances);
    }

    private boolean matchesDate(JSONObject json, Date date) {
        return json
                .getJSONArray("BD")
                .toList()
                .stream()
                .map(Map.class::cast)
                .map(JSONObject::new)
                .map(sd -> sd.getString("date"))
                .anyMatch(d -> df.format(date).equals(d));
    }

}
