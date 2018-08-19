package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import repertuar.model.Seance;

import java.util.function.Function;

import static repertuar.model.Chain.HELIOS;

public class SeanceExtractor implements Function<Element, Seance> {

    @Override
    public Seance apply(Element element) {
        return new Seance(element.text(), getSeanceUrl(element));
    }

    private String getSeanceUrl(Element element) {
        return HELIOS.getBaseUrl() + element.attr("href");
    }

}
