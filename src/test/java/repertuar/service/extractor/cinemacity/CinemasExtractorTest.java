package repertuar.service.extractor.cinemacity;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import repertuar.model.Cinema;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class CinemasExtractorTest {

    private final Function<JSONObject, List<Cinema>> cinemasExtractor = new CinemasExtractor();

    @Test
    public void shouldReturnListOfCinemaObjectsForGivenJSONObject() throws IOException {
        //given
        String json = IOUtils.toString(this.getClass().getResourceAsStream("/cinemacity/cinemas.json"), "UTF-8");
        JSONObject jsonObject = new JSONObject(json);

        //when
        List<Cinema> cinemas = cinemasExtractor.apply(jsonObject);

        //then
        then(cinemas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);
    }
}