package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import repertuar.utils.Website;

import java.util.List;

public class Film {
    private final SimpleStringProperty title;
    private final Website website;
    private final SimpleListProperty<Seance> seances;

    public Film(String title, String url, List<Seance> hours) {
        this.title = new SimpleStringProperty(title);
        this.website = new Website(url);
        this.seances = new SimpleListProperty<>(FXCollections.observableList(hours));
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleListProperty<Seance> getHours() {
        return seances;
    }

    public Website getWebsite() {
        return website;
    }

}
