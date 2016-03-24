package repertuar.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.helios.Helios;
import repertuar.model.multikino.Multikino;

import java.util.LinkedList;

/**
 * Created by mateu on 22.09.2015.
 */
public class RepertuarModel {

    private final SimpleListProperty<Chain> chains;

    public RepertuarModel() {
        LinkedList tmp = new LinkedList();
        tmp.add(new Helios("Helios", new Website("http://helios.pl")));
        tmp.add(new Multikino("Multikino", new Website("http://multikino.pl")));
        tmp.add(new CinemaCity("Cinema-City", new Website("http://www.cinema-city.pl/")));

        this.chains = new SimpleListProperty<>(FXCollections.observableList(tmp));

    }

    public SimpleListProperty<Chain> getChains() {
        return chains;
    }


}
