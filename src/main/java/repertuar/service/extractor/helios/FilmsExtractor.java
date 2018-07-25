package repertuar.service.extractor.helios;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import repertuar.model.Film;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FilmsExtractor implements Function<Document, List<Film>> {

    private final Function<Element, Film> filmExtractor = new FilmExtractor();

    @Override
    public List<Film> apply(Document document) {
        return document
                .body()
                .select("li.seance")
                .stream()
                .map(filmExtractor)
                .collect(Collectors.toList());
    }

}
