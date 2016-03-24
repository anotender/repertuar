package repertuar.model.multikino;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import repertuar.model.Cinema;
import repertuar.model.Website;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by anotender on 07.12.15.
 */
public class MultikinoCinema extends Cinema {

    public MultikinoCinema(String name, String city, String website) {
        super(name, city, website);
    }

    @Override
    public void loadDays() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Website w = new Website(website.toString().substring(0, website.toString().length() - 2) + "#" + dateFormat.format(date));
        w.loadContent(false);

        boolean isReadingDay = false;

        String dateString = null;
        String dow = null;//day of week
        String dom = null;//day of month
        String mon = null;//month

        for (String line : w.getContent()) {
            if (!isReadingDay && line.contains("data-date")) {
                isReadingDay = true;
                dateString = line.substring(line.indexOf("data-date=\"") + "data-date=\"".length(), line.indexOf("\">"));
            } else if (isReadingDay && line.contains("class=\"dow\"")) {
                dow = line.replaceAll("<.*?>", "").trim();
            } else if (isReadingDay && line.contains("day")) {
                dom = line.replaceAll("<.*?>", "").trim();
            } else if (isReadingDay && line.contains("mon")) {
                mon = line.replaceAll("<.*?>", "").trim();
                isReadingDay = false;

                days.add(new Pair<>(dow + " " + dom + " " + mon + ":" + dateString, new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()))));
            }
        }
    }

    @Override
    public void loadFilms(int day, String date) {

        Website w = new Website(website.toString() + "#" + date.substring(0, date.indexOf(":")));
        w.loadContent(true);

        boolean isReadingHours = false;
        String url = null, title = null;
        LinkedList<Pair<SimpleStringProperty, Website>> hours = new LinkedList<>();

        for (String line : w.getContent()) {
            if (line.contains("cinema-overlay")) {
                break;
            }
            if (line.contains("class=\"title\"")) {
                line = line.trim();
                url = "http://multikino.pl" + line.substring(line.indexOf("href=\"") + "href=\"".length(), line.indexOf("class") - 2);
                title = line.replaceAll("<.*?>", "").trim();
                continue;
            }
            if (!isReadingHours && line.contains("bt-event-right-col")) {
                isReadingHours = true;
            } else if (isReadingHours && line.trim().matches("[0-2][0-9]:[0-5][0-9]")) {
                hours.add(new Pair<>(new SimpleStringProperty(line.trim()), null));
            } else if (isReadingHours && line.contains("</div>")) {
                days.get(day).getValue().add(new MultikinoFilm(title, url, hours));
                hours.clear();
                isReadingHours = false;
            }
        }

    }
}
