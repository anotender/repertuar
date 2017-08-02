package repertuar.service.impl;

import org.json.JSONArray;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.service.api.ChainService;
import repertuar.utils.HttpUtils;
import repertuar.utils.RepertoireUtils;

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
        return Collections.emptyList();
    }

    private CinemaCityCinema prepareCinema(Map m) {
        Integer id = Integer.parseInt(m.get("id").toString());
        String name = m.get("n").toString();
        String url = "https://www.cinema-city.pl" + m.get("url").toString();
        return new CinemaCityCinema(id, name, url);
    }

}
