package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Seance;
import repertuar.model.cinemacity.CinemaCity;

import java.util.function.Function;

public class SeanceExtractor implements Function<JSONObject, Seance> {

    @Override
    public Seance apply(JSONObject jsonObject) {
        return new Seance(
                jsonObject.getString("eventDateTime"),
                CinemaCity.BASE_URL + jsonObject.getString("bookingLink")
        );
    }

}
