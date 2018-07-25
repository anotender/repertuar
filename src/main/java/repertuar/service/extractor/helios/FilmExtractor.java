package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.model.helios.Helios;

import java.util.List;
import java.util.function.Function;

public class FilmExtractor implements Function<Element, Film> {

    private final Function<Element, List<Seance>> seancesExtractor = new SeancesExtractor();

    @Override
    public Film apply(Element element) {
        return new Film(
                "",
                getTitle(element),
                getUrl(element),
                seancesExtractor.apply(element)
        );

    }

    private String getUrl(Element element) {
        return Helios.BASE_URL + "/" + element.select("a.movie-link").attr("href");
    }

    private String getTitle(Element element) {
        return element.select("h2.movie-title").text();
    }

}
