package repertuar.service.extractor.cinemacity;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Seance;
import repertuar.model.cinemacity.CinemaCity;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeancesExtractor implements BiFunction<JSONArray, String, List<Seance>> {

    @Override
    public List<Seance> apply(JSONArray objects, String filmId) {
        return IntStream
                .range(0, objects.length())
                .mapToObj(objects::getJSONObject)
                .filter(jsonObject -> jsonObject.getString("filmId").equals(filmId))
                .map(this::mapJSONObjectToSeance)
                .collect(Collectors.toList());
    }

    private Seance mapJSONObjectToSeance(JSONObject jsonObject) {
        return new Seance(
                jsonObject.getString("eventDateTime"),
                CinemaCity.BASE_URL + jsonObject.getString("bookingLink")
        );
    }

}
