package repertuar.service.extractor.helios;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import repertuar.model.Cinema;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CinemasExtractor implements Function<Document, List<Cinema>> {

    private final Function<Element, Cinema> cinemaExtractor = new CinemaExtractor();

    @Override
    public List<Cinema> apply(Document document) {
        return document
                .body()
                .select("section.cinema-list")
                .select("div.list")
                .select("a[href]")
                .stream()
                .map(cinemaExtractor)
                .collect(Collectors.toList());
    }

}
