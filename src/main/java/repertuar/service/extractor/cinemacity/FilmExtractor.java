package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import repertuar.model.Film;

import java.util.LinkedList;
import java.util.function.Function;

import static repertuar.model.Chain.CINEMA_CITY;

public class FilmExtractor implements Function<JSONObject, Film> {

    @Override
    public Film apply(JSONObject jsonObject) {
        return new Film(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                CINEMA_CITY.getBaseUrl() + jsonObject.getString("link"),
                new LinkedList<>()
        );
    }

}
