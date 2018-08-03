package repertuar.service.extractor.multikino;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import repertuar.model.Cinema;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CinemasExtractor implements Function<Document, List<Cinema>> {

    private final Function<Element, Cinema> cinemaExtractor = new CinemaExtractor();

    @Override
    public List<Cinema> apply(Document cinemasDocument) {
        return cinemasDocument
                .body()
                .select("li.ml-columns__item")
                .select("a")
                .parallelStream()
                .map(cinemaExtractor)
                .collect(Collectors.toList());
    }


}
