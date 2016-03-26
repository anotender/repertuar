package repertuar.model.multikino;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import repertuar.model.Film;
import repertuar.model.Website;

import java.util.LinkedList;

public class MultikinoFilm extends Film {

    public MultikinoFilm(String title, String url, LinkedList<Pair<SimpleStringProperty, Website>> hours) {
        super(title, url, hours);
    }

    @Override
    public void loadHours() {

    }
}
