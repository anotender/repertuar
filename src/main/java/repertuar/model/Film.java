package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.util.LinkedList;

public abstract class Film {

    protected final SimpleStringProperty title;
    protected final Website website;
    protected final SimpleListProperty<Pair<SimpleStringProperty, Website>> hours = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

    public Film(String title, String url) {
        this.title = new SimpleStringProperty(title);
        this.website = new Website(url);
    }

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

    public abstract void loadHours();

}
