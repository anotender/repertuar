package repertuar.service.extractor.helios;

import org.jsoup.nodes.Element;
import repertuar.model.Seance;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SeancesExtractor implements Function<Element, List<Seance>> {

    private final Function<Element, Seance> seanceExtractor = new SeanceExtractor();

    @Override
    public List<Seance> apply(Element element) {
        return element
                .select("a.hour-link.fancybox-reservation")
                .stream()
                .map(seanceExtractor)
                .collect(Collectors.toList());
    }

}
