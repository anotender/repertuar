package repertuar.model.helios;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import repertuar.model.Cinema;
import repertuar.model.Website;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HeliosCinema extends Cinema {

    public HeliosCinema(String name, String city, String url) {
        super(name, city, url);
    }

    @Override
    public void loadDays() throws IOException {
        Website website = new Website(url.get().replace("StronaGlowna/", "Repertuar/index/dzien/"));
        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> liElements = page.getElementsByTagName("li");

        for (DomElement liElement : liElements) {
            if (liElement.hasAttribute("class") && liElement.getAttribute("class").contains("day")) {
                String dayOfWeek = liElement.getElementsByTagName("abbr").get(0).getTextContent();
                String dayOfMonth = liElement.getElementsByTagName("strong").get(0).getTextContent();
                String month = liElement.getElementsByTagName("em").get(0).getTextContent();

                days.add(new Pair<>(dayOfWeek + " " + dayOfMonth + " " + month,
                        new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>())))
                );
            }
        }
    }

    @Override
    public void loadFilms(int day, String date) throws IOException {
        Website website = new Website(url.get().replace("StronaGlowna/", "Repertuar/index/dzien/" + day));
        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> liElements = page.getElementsByTagName("li");

        for (DomElement liElement : liElements) {
            if (liElement.hasAttribute("class") && liElement.getAttribute("class").equals("seance")) {
                List<HtmlElement> aElements = liElement.getElementsByTagName("a");
                LinkedList<Pair<SimpleStringProperty, Website>> hours = new LinkedList<>();
                String url = null, title = null;
                for (HtmlElement aElement : aElements) {
                    if (aElement.hasAttribute("class") && aElement.getAttribute("class").equals("movie-link")) {
                        url = "http://helios.pl" + aElement.getAttribute("href");
                        title = aElement.getTextContent().trim();
                    } else if (aElement.hasAttribute("class") && aElement.getAttribute("class").equals("hour-link fancybox-reservation")) {
                        String hour = aElement.getTextContent();
                        String hourUrl = "http://helios.pl" + aElement.getAttribute("href");

                        hours.add(new Pair<>(
                                new SimpleStringProperty(hour),
                                new Website(hourUrl)
                        ));
                    }
                }
                if (!hours.isEmpty())
                    days.get(day).getValue().add(new HeliosFilm(title, url, hours));
            }
        }
    }
}
