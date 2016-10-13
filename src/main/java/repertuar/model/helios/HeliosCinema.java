package repertuar.model.helios;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import repertuar.model.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HeliosCinema extends Cinema {

    public HeliosCinema(String name, String city, String url) {
        super(name, city, url);
    }

    @Override
    public void loadDays() throws IOException {
        new Website(url.get().replace("StronaGlowna/", "Repertuar/index/dzien/"))
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("li")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").contains("day"))
                .map(this::extractSeanceDayFromDomElement)
                .forEach(days::add);
    }

    @Override
    public void loadFilms(int day, String date) throws IOException {
        new Website(url.get().replace("StronaGlowna/", "Repertuar/index/dzien/" + day))
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("li")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").equals("seance"))
                .forEach(e -> {
                    List<HtmlElement> aElements = e.getElementsByTagName("a");
                    LinkedList<Seance> hours = new LinkedList<>();
                    String url = null, title = null;
                    for (HtmlElement aElement : aElements) {
                        if (aElement.hasAttribute("class") && aElement.getAttribute("class").equals("movie-link")) {
                            url = "http://helios.pl" + aElement.getAttribute("href");
                            title = aElement.getTextContent().trim();
                        } else if (aElement.hasAttribute("class") && aElement.getAttribute("class").equals("hour-link fancybox-reservation")) {
                            String hour = aElement.getTextContent();
                            String hourUrl = "http://helios.pl" + aElement.getAttribute("href");

                            hours.add(new Seance(hour, hourUrl));
                        }
                    }
                    if (!hours.isEmpty())
                        days.get(day).getFilms().add(new Film(title, url, hours));
                });
    }

    private SeanceDay extractSeanceDayFromDomElement(DomElement e) {
        String dayOfWeek = e.getElementsByTagName("abbr").get(0).getTextContent();
        String dayOfMonth = e.getElementsByTagName("strong").get(0).getTextContent();
        String month = e.getElementsByTagName("em").get(0).getTextContent();

        return new SeanceDay(
                dayOfWeek + " " + dayOfMonth + " " + month,
                new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()))
        );
    }
}
