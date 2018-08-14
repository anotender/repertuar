package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import org.junit.Test;
import repertuar.model.Cinema;
import repertuar.service.extractor.JSONExtractorTest;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class CinemasExtractorTest extends JSONExtractorTest {

    private final Function<JSONObject, List<Cinema>> cinemasExtractor = new CinemasExtractor();

    @Test
    public void shouldReturnListOfCinemaObjectsForGivenJSONObject() throws IOException {
        //given
        JSONObject jsonObject = getResource("/cinemacity/cinemas.json");

        //when
        List<Cinema> cinemas = cinemasExtractor.apply(jsonObject);

        //then
        then(cinemas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);
    }
}