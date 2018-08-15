package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import org.junit.Test;
import repertuar.model.Seance;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class SeanceExtractorTest extends HTMLExtractorTest {

    private final Function<Element, Seance> seanceExtractor = new SeanceExtractor();

    @Test
    public void shouldReturnSeanceObjectForGivenHTMLElement() throws IOException {
        //given
        Element seanceElement = getResource("/helios/seance.html").body().selectFirst("a");

        //when
        Seance seance = seanceExtractor.apply(seanceElement);

        //then
        then(seance).isNotNull();

        then(seance.getHour())
                .isNotEmpty()
                .isEqualTo("22:00");

        then(seance.getUrl())
                .isNotEmpty()
                .isEqualTo("http://helios.pl/38,Bydgoszcz/Seans/info/seans/7B3FCA04-E010-45B5-AF62-C97577F6D630/dzien/120337/film/11750");
    }

}