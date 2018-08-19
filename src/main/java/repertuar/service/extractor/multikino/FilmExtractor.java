package repertuar.service.extractor.multikino;

import org.json.JSONArray;
import org.json.JSONObject;
import repertuar.model.Film;
import repertuar.model.Seance;

import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;

import static repertuar.model.Chain.MULTIKINO;

public class FilmExtractor implements BiFunction<JSONObject, Date, Film> {

    private final BiFunction<JSONArray, Date, List<Seance>> seancesExtractor = new SeancesExtractor();

    @Override
    public Film apply(JSONObject filmJSONObject, Date date) {
        return new Film(
                filmJSONObject.getString("id"),
                filmJSONObject.getString("title"),
                MULTIKINO.getBaseUrl() + filmJSONObject.getString("filmlink"),
                seancesExtractor.apply(filmJSONObject.getJSONArray("showings"), date)
        );
    }

}
