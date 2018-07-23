package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.cinemacity.CinemaCityCinema;

import java.util.function.Function;

public class CinemaExtractor implements Function<JSONObject, Cinema> {

    @Override
    public Cinema apply(JSONObject cinemaJSONObject) {
        Integer id = cinemaJSONObject.getInt("id");
        String name = cinemaJSONObject.getString("displayName");
        String baseUrl = "https://www.cinema-city.pl";
        String url = baseUrl + cinemaJSONObject.getString("link");
        return new CinemaCityCinema(id, name, url);
    }

}
