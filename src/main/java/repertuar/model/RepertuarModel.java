package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;
import repertuar.utils.Website;

import java.util.LinkedList;

public class RepertuarModel {

    private final SimpleListProperty<Chain> chains;

    public RepertuarModel() {
        chains = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        chains.add(new Helios("Helios", new Website("http://helios.pl")));
        chains.add(new Multikino("Multikino", new Website("http://multikino.pl")));
        chains.add(new CinemaCity("Cinema-City", new Website("http://www.cinema-city.pl/")));
    }

    public SimpleListProperty<Chain> getChains() {
        return chains;
    }
}