package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import org.junit.Test;
import repertuar.model.Seance;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class SeancesExtractorTest extends HTMLExtractorTest {

    private final Function<Element, List<Seance>> seancesExtractor = new SeancesExtractor();

    @Test
    public void shouldReturnSeanceObjectForGivenHTMLElement() throws IOException {
        //given
        Element htmlSeanceElement = getResource("/helios/seances.html").body().selectFirst("li");

        //when
        List<Seance> seances = seancesExtractor.apply(htmlSeanceElement);

        //then
        then(seances)
                .isNotEmpty()
                .hasSize(7);

        then(seances)
                .extracting("hour")
                .containsExactly("17:00", "18:00", "19:00", "19:30", "20:45", "21:30", "22:00");
    }

}