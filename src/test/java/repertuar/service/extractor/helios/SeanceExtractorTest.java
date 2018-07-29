package repertuar.service.extractor.helios;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import repertuar.model.Seance;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.*;

public class SeanceExtractorTest {

    private final Function<Element, Seance> seanceExtractor = new SeanceExtractor();

    @Test
    public void shouldReturnSeanceObjectForGivenHTMLElement() throws IOException {
        //given
        String html = IOUtils.toString(this.getClass().getResourceAsStream("/helios/seance.html"), "UTF-8");
        Element htmlSeanceElement = Jsoup.parse(html).body().selectFirst("a");

        //when
        Seance seance = seanceExtractor.apply(htmlSeanceElement);

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