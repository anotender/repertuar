package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.util.LinkedList;

public class Film {

    private final SimpleStringProperty title;
    private final Website website;
    private final SimpleListProperty<Pair<SimpleStringProperty, Website>> hours = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

    public Film(String title, String url, LinkedList<Pair<SimpleStringProperty, Website>> hours) {
        this.title = new SimpleStringProperty(title);
        this.website = new Website(url);
        this.hours.addAll(hours);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleListProperty<Pair<SimpleStringProperty, Website>> getHours() {
        return hours;
    }

    public Website getWebsite() {
        return website;
    }

}
