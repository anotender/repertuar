package repertuar.model.multikino;

import com.gargoylesoftware.htmlunit.html.DomElement;
import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Website;

import java.io.IOException;

public class Multikino extends Chain {

    public Multikino(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        new Website("https://multikino.pl/pl/nasze-kina")
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("a")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").equals("title"))
                .map(this::extractCinema)
                .forEach(cinemas::add);
    }

    private Cinema extractCinema(DomElement e) {
        String url = "https://multikino.pl" + e.getAttribute("href");
        String city = e.getTextContent();
        int cinemaNumber = Integer.parseInt(e.getAttribute("rel"));
        return new MultikinoCinema(null, city, url, cinemaNumber);
    }
}
