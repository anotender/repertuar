package repertuar.service.extractor.helios;

import org.jsoup.nodes.Document;
import org.junit.Test;
import repertuar.model.Cinema;
import repertuar.model.helios.HeliosCinema;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class CinemasExtractorTest extends HTMLExtractorTest {

    private final Function<Document, List<Cinema>> cinemasExtractor = new CinemasExtractor();

    @Test
    public void shouldReturnListOfHeliosCinemasForGivenHTMLDocument() throws IOException {
        //given
        Document htmlCinemasDocument = getResource("/helios/cinemas.html");

        //when
        List<Cinema> cinemas = cinemasExtractor.apply(htmlCinemasDocument);

        //then
        then(cinemas)
                .isNotEmpty()
                .hasSize(3)
                .doesNotContainNull()
                .hasOnlyElementsOfType(HeliosCinema.class);
    }
}