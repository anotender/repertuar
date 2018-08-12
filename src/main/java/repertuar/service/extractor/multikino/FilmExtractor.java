package repertuar.service.extractor.multikino;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.multikino.Multikino;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilmExtractor implements BiFunction<JSONObject, Date, Film> {

    @Override
    public Film apply(JSONObject filmJSONObject, Date date) {
        return new Film(
                filmJSONObject.getString("id"),
                filmJSONObject.getString("title"),
                Multikino.BASE_URL + filmJSONObject.getString("filmlink"),
                getSeances(filmJSONObject, date)
        );
    }

    private List<Seance> getSeances(JSONObject filmJSONObject, Date date) {
        JSONArray showings = filmJSONObject.getJSONArray("showings");

        return IntStream
                .range(0, showings.length())
                .mapToObj(showings::getJSONObject)
                .filter(s -> {
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        return DateUtils.isSameDay(date, df.parse(s.getString("date_time")));
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .flatMap(s -> {
                    JSONArray times = s.getJSONArray("times");
                    return IntStream
                            .range(0, times.length())
                            .mapToObj(times::getJSONObject);
                })
                .map(this::prepareSeance)
                .collect(Collectors.toList());
    }

    private Seance prepareSeance(JSONObject seanceJSONObject) {
        return new Seance(seanceJSONObject.getString("time"), "");
    }

}
