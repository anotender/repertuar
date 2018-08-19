package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Cinema;
import repertuar.model.cinemacity.CinemaCityCinema;

import java.util.function.Function;

import static repertuar.model.Chain.CINEMA_CITY;

public class CinemaExtractor implements Function<JSONObject, Cinema> {

    @Override
    public Cinema apply(JSONObject cinemaJSONObject) {
        return new CinemaCityCinema(
                cinemaJSONObject.getInt("id"),
                cinemaJSONObject.getString("displayName"),
                CINEMA_CITY.getBaseUrl() + cinemaJSONObject.getString("link")
        );
    }

}
