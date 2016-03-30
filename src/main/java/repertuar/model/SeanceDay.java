package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SeanceDay {
    private SimpleStringProperty stringToShow;
    private SimpleListProperty<Film> films;

    public SeanceDay(String stringToShow, ObservableList<Film> films) {
        this.stringToShow = new SimpleStringProperty(stringToShow);
        this.films = new SimpleListProperty<>(FXCollections.observableList(films));
    }

    public String getStringToShow() {
        return stringToShow.get();
    }

    public SimpleStringProperty stringToShowProperty() {
        return stringToShow;
    }

    public void setStringToShow(String stringToShow) {
        this.stringToShow.set(stringToShow);
    }

    public SimpleListProperty<Film> filmsProperty() {
        return films;
    }

    public void setFilms(ObservableList<Film> films) {
        this.films.set(films);
    }

    public ObservableList<Film> getFilms() {
        return films.get();
    }
}
