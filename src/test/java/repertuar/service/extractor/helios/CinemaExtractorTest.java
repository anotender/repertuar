package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import org.junit.Test;
import repertuar.model.Cinema;
import repertuar.model.helios.HeliosCinema;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class CinemaExtractorTest extends HTMLExtractorTest {

    private final Function<Element, Cinema> cinemaExtractor = new CinemaExtractor();

    @Test
    public void shouldReturnHeliosCinemaObjectForGivenHTMLElement() throws IOException {
        //given
        Element htmlCinemaElement = getResource("/helios/cinema.html").body().selectFirst("a");

        //when
        Cinema cinema = cinemaExtractor.apply(htmlCinemaElement);

        //then
        then(cinema)
                .isNotNull()
                .isInstanceOf(HeliosCinema.class);

        then(cinema.getId()).isEqualTo(37);

        then(cinema.getName())
                .isNotEmpty()
                .isEqualTo("Bełchatów");

        then(cinema.getUrl())
                .isNotEmpty()
                .isEqualTo("http://helios.pl/37,Belchatow/StronaGlowna/");
    }
}