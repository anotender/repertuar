package repertuar.service.extractor.cinemacity;

import org.json.JSONObject;
import org.junit.Test;
import repertuar.model.Cinema;
import repertuar.model.cinemacity.CinemaCityCinema;
import repertuar.service.extractor.JSONExtractorTest;
import repertuar.service.impl.CinemaCityService;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class CinemaExtractorTest extends JSONExtractorTest {

    private final Function<JSONObject, Cinema> cinemaExtractor = new CinemaExtractor();

    @Test
    public void shouldReturnCinemaCityCinemaObjectForGivenJSONObject() throws IOException {
        //given
        JSONObject jsonObject = getResource("/cinemacity/cinema.json");

        //when
        Cinema cinema = cinemaExtractor.apply(jsonObject);

        //then
        then(cinema)
                .isNotNull()
                .isInstanceOf(CinemaCityCinema.class);

        then(cinema.getId())
                .isNotNull()
                .isEqualTo(1088);

        then(cinema.getName())
                .isNotBlank()
                .isEqualTo("Bielsko-Biała");

        then(cinema.getUrl())
                .isNotBlank()
                .isEqualTo("https://www.cinema-city.pl/kina/bielsko-biala");

        then(cinema.getService())
                .isNotNull()
                .isInstanceOf(CinemaCityService.class);
    }

}