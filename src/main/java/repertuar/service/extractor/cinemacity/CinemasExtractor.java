package repertuar.service.extractor.cinemacity;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Cinema;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CinemasExtractor implements Function<JSONObject, List<Cinema>> {

    private final Function<JSONObject, Cinema> cinemaExtractor = new CinemaExtractor();

    @Override
    public List<Cinema> apply(JSONObject cinemasJSONObject) {
        List<Cinema> cinemas = new LinkedList<>();

        JSONArray cinemasJSONArray = cinemasJSONObject
                .getJSONObject("body")
                .getJSONArray("cinemas");

        for (int i = 0; i < cinemasJSONArray.length(); i++) {
            JSONObject cinemaJSONObject = cinemasJSONArray.getJSONObject(i);
            cinemas.add(cinemaExtractor.apply(cinemaJSONObject));
        }

        return cinemas;
    }

}
