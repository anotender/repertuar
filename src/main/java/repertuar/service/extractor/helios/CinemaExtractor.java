package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import repertuar.model.Cinema;
import repertuar.model.helios.Helios;
import repertuar.model.helios.HeliosCinema;

import java.util.function.Function;

public class CinemaExtractor implements Function<Element, Cinema> {

    @Override
    public Cinema apply(Element element) {
        return new HeliosCinema(getId(element), getName(element), getUrl(element));
    }

    private String getName(Element element) {
        return element.select("strong").text() + element.select("span").text().replace("Helios", "");
    }

    private int getId(Element element) {
        return Integer.parseInt(element.attr("href").substring(1, element.attr("href").indexOf(",")));
    }

    private String getUrl(Element element) {
        return Helios.BASE_URL + element.attr("href");
    }

}
