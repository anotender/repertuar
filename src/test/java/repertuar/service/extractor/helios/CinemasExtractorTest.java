package repertuar.service.extractor.helios;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import repertuar.model.Cinema;
import repertuar.model.helios.HeliosCinema;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.*;

public class CinemasExtractorTest {

    private final Function<Document, List<Cinema>> cinemasExtractor = new CinemasExtractor();

    @Test
    public void shouldReturnListOfHeliosCinemasForGivenHTMLDocument() throws IOException {
        //given
        String html = IOUtils.toString(this.getClass().getResourceAsStream("/helios/cinemas.html"), "UTF-8");
        Document htmlCinemasDocument = Jsoup.parse(html);

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