package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import repertuar.model.Seance;
import repertuar.model.helios.Helios;

import java.util.function.Function;

public class SeanceExtractor implements Function<Element, Seance> {

    @Override
    public Seance apply(Element element) {
        return new Seance(element.text(), getSeanceUrl(element));
    }

    private String getSeanceUrl(Element element) {
        return Helios.BASE_URL + "/" + element.attr("href");
    }

}
