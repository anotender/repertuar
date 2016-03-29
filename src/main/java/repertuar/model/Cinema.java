package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.io.IOException;
import java.util.LinkedList;

public abstract class Cinema {

    protected final SimpleStringProperty name;
    protected final SimpleStringProperty city;
    protected final SimpleStringProperty url;
    protected final SimpleListProperty<Pair<String, SimpleListProperty<Film>>> days = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

    public Cinema(String name, String city, String url) {
        this.name = new SimpleStringProperty(name);
        this.city = new SimpleStringProperty(city);
        this.url = new SimpleStringProperty(url);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public SimpleListProperty getDays() {
        return days;
    }

    public abstract void loadDays() throws IOException;

    public abstract void loadFilms(int day, String date) throws IOException;

}
