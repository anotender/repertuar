package repertuar.service.extractor.multikino;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import repertuar.model.Cinema;
import repertuar.model.multikino.Multikino;
import repertuar.model.multikino.MultikinoCinema;

import java.util.function.Function;

public class CinemaExtractor implements Function<Element, Cinema> {

    @Override
    public Cinema apply(Element element) {
        String name = element.text();
        String url = Multikino.BASE_URL + element.attr("href");
        Integer id = fetchCinemaId(url);
        return new MultikinoCinema(id, name, url);
    }

    private Integer fetchCinemaId(String url) {
        try {
            String stringId = Jsoup
                    .connect(url)
                    .get()
                    .body()
                    .attr("data-selected-locationid");

            return Integer.parseInt(stringId);
        } catch (Exception ignored) {
        }
        return 0;
    }

}
