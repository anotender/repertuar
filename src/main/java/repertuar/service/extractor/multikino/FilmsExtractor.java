package repertuar.service.extractor.multikino;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Film;

import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilmsExtractor implements BiFunction<JSONArray, Date, List<Film>> {

    private final BiFunction<JSONObject, Date, Film> filmExtractor = new FilmExtractor();

    @Override
    public List<Film> apply(JSONArray filmsJSONArray, Date date) {
        return IntStream
                .range(0, filmsJSONArray.length())
                .mapToObj(filmsJSONArray::getJSONObject)
                .filter(this::isNowPlaying)
                .map(f -> filmExtractor.apply(f, date))
                .collect(Collectors.toList());
    }

    private boolean isNowPlaying(JSONObject filmJSONObject) {
        boolean hasSeances = filmJSONObject.getInt("original_s_count") > 0;
        boolean comingSoon = filmJSONObject.getBoolean("coming_soon");
        boolean announcement = filmJSONObject.getBoolean("announcement");
        return hasSeances && !comingSoon && !announcement;
    }

}
