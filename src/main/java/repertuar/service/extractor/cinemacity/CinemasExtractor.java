package repertuar.service.extractor.cinemacity;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Cinema;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CinemasExtractor implements Function<JSONObject, List<Cinema>> {

    private final Function<JSONObject, Cinema> cinemaExtractor = new CinemaExtractor();

    @Override
    public List<Cinema> apply(JSONObject cinemasJSONObject) {
        JSONArray cinemasJSONArray = cinemasJSONObject
                .getJSONObject("body")
                .getJSONArray("cinemas");

        return IntStream
                .range(0, cinemasJSONArray.length())
                .mapToObj(cinemasJSONArray::getJSONObject)
                .map(cinemaExtractor)
                .collect(Collectors.toList());
    }

}
