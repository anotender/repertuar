package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Chain {

    protected String name;
    protected Website website;
    protected final SimpleListProperty<Cinema> cinemas = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    public Chain(String name, Website website) {
        this.name = name;
        this.website = website;
    }

    public SimpleListProperty<Cinema> getCinemas() {
        return cinemas;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract void loadCinemas() throws IOException;

}
