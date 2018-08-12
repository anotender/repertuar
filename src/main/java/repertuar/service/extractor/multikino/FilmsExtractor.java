package repertuar.service.extractor.multikino;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Film;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilmsExtractor implements BiFunction<JSONObject, Date, List<Film>> {

    private final BiFunction<JSONObject, Date, Film> filmExtractor = new FilmExtractor();

    @Override
    public List<Film> apply(JSONObject filmsJSONObject, Date date) {
        JSONArray filmsJSONArray = filmsJSONObject
                .getJSONArray("films");

        return IntStream
                .range(0, filmsJSONArray.length())
                .mapToObj(filmsJSONArray::getJSONObject)
                .filter(this::isNowPlaying)
                .filter(filmJSONObject -> matchesDate(filmJSONObject, date))
                .map(f -> filmExtractor.apply(f, date))
                .collect(Collectors.toList());
    }

    private boolean isNowPlaying(JSONObject filmJSONObject) {
        boolean hasSeances = filmJSONObject.getInt("original_s_count") > 0;
        boolean comingSoon = filmJSONObject.getBoolean("coming_soon");
        boolean announcement = filmJSONObject.getBoolean("announcement");
        return hasSeances && !comingSoon && !announcement;
    }

    private boolean matchesDate(JSONObject filmJSONObject, Date date) {
        JSONArray showings = filmJSONObject
                .getJSONArray("showings");

        return IntStream
                .range(0, showings.length())
                .mapToObj(showings::getJSONObject)
                .map(s -> s.getString("date_time"))
                .map(this::getDateFromString)
                .anyMatch(d -> DateUtils.isSameDay(date, d));
    }

    private Date getDateFromString(String stringDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(stringDate);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
