package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Seance;

import java.util.function.Function;

import static repertuar.model.Chain.CINEMA_CITY;

public class SeanceExtractor implements Function<JSONObject, Seance> {

    @Override
    public Seance apply(JSONObject jsonObject) {
        return new Seance(
                jsonObject.getString("eventDateTime"),
                CINEMA_CITY.getBaseUrl() + jsonObject.getString("bookingLink")
        );
    }

}
