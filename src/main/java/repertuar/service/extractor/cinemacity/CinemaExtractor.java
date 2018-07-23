package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.cinemacity.CinemaCity;
import repertuar.model.cinemacity.CinemaCityCinema;

import java.util.function.Function;

public class CinemaExtractor implements Function<JSONObject, Cinema> {

    @Override
    public Cinema apply(JSONObject cinemaJSONObject) {
        return new CinemaCityCinema(
                cinemaJSONObject.getInt("id"),
                cinemaJSONObject.getString("displayName"),
                CinemaCity.BASE_URL + cinemaJSONObject.getString("link")
        );
    }

}
