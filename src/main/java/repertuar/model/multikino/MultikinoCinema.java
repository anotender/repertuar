package repertuar.model.multikino;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import repertuar.model.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class MultikinoCinema extends Cinema {

    private int cinemaNumber;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MultikinoCinema(String name, String city, String website, int cinemaNumber) {
        super(name, city, website);
        this.cinemaNumber = cinemaNumber;
    }

    @Override
    public void loadDays() throws IOException {
        new Website(prepareCinemaRepertoireUrl())
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("div")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").equals("day-item"))
                .map(e -> {
                    String dateString = e.getTextContent().replaceAll("\n", "").trim().replaceAll("\\s+", " ");
                    return new SeanceDay(
                            dateString + ":" + e.getAttribute("data-date"),
                            new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()))
                    );
                })
                .forEach(days::add);
    }

    @Override
    public void loadFilms(int day, String date) throws IOException {
        new Website(prepareDayRepertoireUrl(date))
                .loadPageWithJavaScriptDisabled()
                .getElementsByTagName("li")
                .stream()
                .filter(e -> e.hasAttribute("class") && e.getAttribute("class").contains("genre-7"))
                .forEach(e -> {
                    LinkedList<Seance> hours = new LinkedList<>();
                    e.getElementsByTagName("a").forEach(htmlElement -> {
                        if ("showing-popup-trigger active".equals(htmlElement.getAttribute("class"))) {
                            String seanceUrl = "https://multikino.pl/kup-bilet2/";
                            seanceUrl += htmlElement.getAttribute("data-eventid");
                            seanceUrl += "/";
                            seanceUrl += cinemaNumber;
                            seanceUrl += "/";
                            seanceUrl += htmlElement.getAttribute("datatype");
                            seanceUrl += "/";
                            seanceUrl += htmlElement.getAttribute("data-seanceid");
                            seanceUrl += "/wybierz-miejsce";

                            hours.add(
                                    new Seance(
                                            htmlElement.getTextContent().trim(),
                                            seanceUrl
                                    )
                            );
                        } else if ("title".equals(htmlElement.getAttribute("class"))) {
                            String title = htmlElement.getTextContent().trim();
                            String url = htmlElement.getAttribute("href");
                            days.get(day).getFilms().add(new Film(title, url, hours));
                        }
                    });
                });
    }

    private String prepareCinemaRepertoireUrl() {
        return url.get().substring(0, url.get().length() - 2).replaceAll("wszystkie-kina", "repertuar")
                + "/" + dateFormat.format(new Date());
    }

    private String prepareDayRepertoireUrl(String date) {
        return url.get().substring(0, url.get().length() - 2).replaceAll("wszystkie-kina", "repertuar")
                + "/" + date.substring(date.indexOf(":") + 1);
    }


}
