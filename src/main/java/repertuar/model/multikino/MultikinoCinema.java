package repertuar.model.multikino;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import repertuar.model.Cinema;
import repertuar.model.Website;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MultikinoCinema extends Cinema {

    public MultikinoCinema(String name, String city, String website) {
        super(name, city, website);
    }

    @Override
    public void loadDays() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Website website = new Website(
                url.get().substring(0, url.get().length() - 2).replaceAll("wszystkie-kina", "repertuar")
                        + "/" + dateFormat.format(date)
        );
        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> divElements = page.getElementsByTagName("div");

        for (DomElement divElement : divElements) {
            if (divElement.hasAttribute("class") && divElement.getAttribute("class").equals("day-item")) {
                String dateString = divElement.getTextContent().replaceAll("\n", "").trim().replaceAll("\\s+", " ");
                days.add(
                        new Pair<>(
                                dateString + ":" + divElement.getAttribute("data-date"),
                                new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()))
                        )
                );
            }
        }
    }

    @Override
    public void loadFilms(int day, String date) throws IOException {
        Website website = new Website(
                url.get().substring(0, url.get().length() - 2).replaceAll("wszystkie-kina", "repertuar")
                        + "/" + date.substring(date.indexOf(":") + 1)
        );
        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> liElements = page.getElementsByTagName("li");

        for (DomElement liElement : liElements) {
            if (liElement.hasAttribute("class") && liElement.getAttribute("class").contains("genre-7")) {
                LinkedList<Pair<SimpleStringProperty, Website>> hours = new LinkedList<>();

                liElement.getElementsByTagName("a").forEach(htmlElement -> {
                    if (htmlElement.getAttribute("class").equals("showing-popup-trigger active")) {
                        hours.add(
                                new Pair<>(
                                        new SimpleStringProperty(htmlElement.getTextContent().trim()),
                                        null//da sie to zrobic
                                )
                        );
                    } else if (htmlElement.getAttribute("class").equals("title")) {
                        String title = htmlElement.getTextContent().trim();
                        String url = htmlElement.getAttribute("href");
                        days.get(day).getValue().add(new MultikinoFilm(title, url, hours));
                    }
                });
            }
        }
    }
}
