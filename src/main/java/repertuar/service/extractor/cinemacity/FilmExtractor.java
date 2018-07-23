package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Film;
import repertuar.model.cinemacity.CinemaCity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Function;

public class FilmExtractor implements Function<JSONObject, Film> {

    @Override
    public Film apply(JSONObject jsonObject) {
        return new Film(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                CinemaCity.BASE_URL + jsonObject.getString("link"),
                new LinkedList<>()
        );
    }

}
