package repertuar.model.multikino;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.utils.HttpUtils;
import repertuar.utils.Website;

import java.io.IOException;
import java.util.Map;

public class Multikino extends Chain {

    public Multikino(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        HttpUtils
                .sendGet("https://multikino.pl/pl/repertoire/cinema/index")
                .getJSONArray("results")
                .toList()
                .stream()
                .filter(o -> o instanceof Map)
                .map(Map.class::cast)
                .map(this::extractCinema)
                .forEach(cinemas::add);
    }

    private Cinema extractCinema(Map m) {
        String name = (String) m.get("name");
        String city = (String) m.get("city");
        String website = "https://multikino.pl/pl/repertuar/" + m.get("slug");
        Integer cinemaID = Integer.parseInt((String) m.get("id"));
        Integer cityID = Integer.parseInt((String) m.get("city_id"));
        return new MultikinoCinema(name, city, website, cinemaID, cityID);
    }
}
