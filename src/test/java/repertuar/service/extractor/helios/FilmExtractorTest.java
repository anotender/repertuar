package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import org.junit.Test;
import repertuar.model.Film;
import repertuar.service.extractor.HTMLExtractorTest;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.BDDAssertions.then;

public class FilmExtractorTest extends HTMLExtractorTest {

    private final Function<Element, Film> filmExtractor = new FilmExtractor();

    @Test
    public void shouldReturnFilmObjectForGivenHTMLElement() throws IOException {
        //given
        Element htmlFilmElement = getResource("/helios/film.html").body().selectFirst("li");

        //when
        Film film = filmExtractor.apply(htmlFilmElement);

        //then
        then(film).isNotNull();

        then(film.getId()).isEmpty();

        then(film.getTitle())
                .isNotEmpty()
                .isEqualTo("Mamma Mia! Here We Go Again / napisy");

        then(film.getSeances())
                .isNotEmpty()
                .hasSize(1);

        then(film.getSeances().get(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("hour", "22:00");

        then(film.getUrl())
                .isNotEmpty()
                .isEqualTo("http://helios.pl//38,Bydgoszcz/BazaFilmow/Szczegoly/film/11750/Mamma-Mia%21-Here-We-Go-Again");
    }
}