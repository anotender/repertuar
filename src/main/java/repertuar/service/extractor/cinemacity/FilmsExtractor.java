package repertuar.service.extractor.cinemacity;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Film;
import repertuar.model.Seance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilmsExtractor implements BiFunction<JSONObject, Date, List<Film>> {

    private final BiFunction<JSONArray, String, List<Seance>> seancesExtractor = new SeancesExtractor();
    private final Function<JSONObject, Film> filmExtractor = new FilmExtractor();

    @Override
    public List<Film> apply(JSONObject filmsJSONObject, Date date) {
        JSONArray filmsJSONArray = filmsJSONObject
                .getJSONObject("body")
                .getJSONArray("films");

        JSONArray seancesJSONArray = filmsJSONObject
                .getJSONObject("body")
                .getJSONArray("events");

        return IntStream
                .range(0, filmsJSONArray.length())
                .mapToObj(filmsJSONArray::getJSONObject)
                .map(filmExtractor)
                .peek(f -> f.setSeances(seancesExtractor.apply(seancesJSONArray, f.getId())))
                .collect(Collectors.toList());
    }

}
