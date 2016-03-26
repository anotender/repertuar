package repertuar.model.cinemaCity;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import repertuar.model.Chain;
import repertuar.model.Website;

import java.io.IOException;
import java.util.List;

public class CinemaCity extends Chain {

    public CinemaCity(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        Website website = new Website("http://www.cinema-city.pl/cinemas");

        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> divElements = page.getElementsByTagName("div");

        for (DomElement divElement : divElements) {
            if (divElement.hasAttribute("class") && divElement.getAttribute("class").equals("cinema_name")) {
                String url = "http://www.cinema-city.pl/" + divElement.getElementsByTagName("a").get(0).getAttribute("href");
                String city = divElement.getElementsByTagName("a").get(0).getTextContent();
                cinemas.add(new CinemaCityCinema(null, city, url));
            }
        }
    }
}
