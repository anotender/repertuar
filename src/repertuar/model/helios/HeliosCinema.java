package repertuar.model.helios;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import repertuar.model.Cinema;
import repertuar.model.Website;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by mateu on 22.09.2015.
 */
public class HeliosCinema extends Cinema {

    public HeliosCinema(String name, String city, String website) {
        super(name, city, website);
    }

    @Override
    public void loadDays() {

        Website w = new Website(website.toString().replace("StronaGlowna/", "Repertuar/index/dzien/"));
        w.loadContent(false);

        boolean isReadingDay = false;

        String dow = "";//day of week
        String dom = "";//day of month
        String mon = "";//month

        for (String s : w.getContent()) {
            if (!isReadingDay && s.contains("day-name")) {
                isReadingDay = true;
                dow = s.replaceAll("<.*?>", "").trim();
            } else if (isReadingDay && s.contains("day-number")) {
                dom = s.replaceAll("<.*?>", "").trim();
            } else if (isReadingDay && s.contains("month-name")) {
                mon = s.replaceAll("<.*?>", "").trim();
                isReadingDay = false;

                days.add(new Pair<>(dow + " " + dom + " " + mon, new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()))));
            }
        }
    }

    @Override
    public void loadFilms(int day, String date) {
        Website w = new Website(website.toString().replace("StronaGlowna/", "Repertuar/index/dzien/" + day));
        w.loadContent(false);

        boolean isReadingFilm = false;
        boolean isReadingHours = false;
        String url = null;
        String title = null;

        for (String line : w.getContent()) {
            if (!isReadingFilm && line.contains("movie-link")) {
                isReadingFilm = true;
                url = "http://helios.pl" + line.substring(line.indexOf("href=\"") + "href=\"".length(), line.indexOf("class") - 2);
            } else if (isReadingFilm && line.contains("</a>")) {
                title = line.replaceAll("<.*?>", "").trim();
                isReadingFilm = false;
                isReadingHours = true;
            } else if (isReadingHours && line.contains("hour-link")) {
                LinkedList<Pair<SimpleStringProperty, Website>> hours = new LinkedList<>();
                String[] splittedLine = line.split("</a>");

                for (String s : splittedLine) {
                    if (s.contains("hour-link")) {
                        String hourLink = "http://helios.pl" + s.substring(s.indexOf("href=\"") + "href=\"".length(), s.lastIndexOf("\">"));
                        String hour = s.replaceAll("<.*?>", "").trim();

                        hours.add(new Pair<>(new SimpleStringProperty(hour), new Website(hourLink)));
                    }
                }

                days.get(day).getValue().add(new HeliosFilm(title, url, hours));
                isReadingHours = false;
            }
        }
    }
}
