package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import org.junit.Test;
import repertuar.model.Film;
import repertuar.service.extractor.JSONExtractorTest;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class FilmExtractorTest extends JSONExtractorTest {

    private final Function<JSONObject, Film> filmExtractor = new FilmExtractor();

    @Test
    public void shouldReturnFilmObjectForGivenJSONObject() throws IOException {
        //given
        JSONObject jsonObject = getResource("/cinemacity/film.json");

        //when
        Film film = filmExtractor.apply(jsonObject);

        //then
        then(film).isNotNull();

        then(film.getId())
                .isNotNull()
                .isEqualTo("2895s2r");

        then(film.getSeances())
                .isNotNull()
                .isEmpty();

        then(film.getTitle())
                .isNotNull()
                .isEqualTo("Zimna wojna");

        then(film.getUrl())
                .isNotNull()
                .isEqualTo("https://www.cinema-city.pl/filmy/zimna-wojna");
    }

}