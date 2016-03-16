package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mateu on 06.12.2015.
 */
public abstract class Cinema {

    protected final SimpleStringProperty name;
    protected final SimpleStringProperty city;
    protected final Website website;
    protected final SimpleListProperty<Pair<String, SimpleListProperty<Film>>> days = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

    public Cinema(String name, String city, String website) {

        this.name = new SimpleStringProperty(name);
        this.city = new SimpleStringProperty(city);
        this.website = new Website(website);

    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public Website getWebsite() {
        return website;
    }

    public SimpleListProperty getDays() {
        return days;
    }

    public abstract void loadDays();

    public abstract void loadFilms(int day, String date);

}
