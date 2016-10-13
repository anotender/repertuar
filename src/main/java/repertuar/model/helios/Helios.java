package repertuar.model.helios;

import repertuar.model.Chain;
import repertuar.model.Website;

import java.io.IOException;

public class Helios extends Chain {

    public Helios(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        website
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("div")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").equals("list"))
                .flatMap(e -> e.getElementsByTagName("a").stream())
                .forEach(htmlElement -> {
                    String url = "http://helios.pl" + htmlElement.getAttribute("href");
                    String city = htmlElement.getElementsByTagName("strong").get(0).getTextContent();
                    String name = htmlElement.getElementsByTagName("span").get(0).getTextContent();
                    cinemas.add(new HeliosCinema(name, city, url));
                });
    }
}
