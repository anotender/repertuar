package repertuar.service.extractor.helios;

import org.jsoup.nodes.Document;
import org.junit.Test;
import repertuar.model.Film;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class FilmsExtractorTest extends HTMLExtractorTest {

    private final Function<Document, List<Film>> filmsExtractor = new FilmsExtractor();

    @Test
    public void shouldReturnListOfFilmsForGivenHTMLDocument() throws IOException {
        //given
        Document filmsDocument = getResource("/helios/films.html");

        //when
        List<Film> films = filmsExtractor.apply(filmsDocument);

        //then
        then(films)
                .hasSize(3)
                .doesNotContainNull()
                .extracting("title")
                .containsExactlyInAnyOrder(
                        "Ostatnie prosecco hrabiego Ancillotto / napisy",
                        "Mission: Impossible - Fallout / napisy",
                        "Ant-Man i Osa / napisy"
                );

    }
}