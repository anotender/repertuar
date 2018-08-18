package repertuar.view;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import repertuar.controller.RepertoireController;
import repertuar.model.Film;
import repertuar.model.Seance;
import repertuar.view.event.ChainChangeEvent;
import repertuar.view.event.CinemaChangeEvent;
import repertuar.view.event.FilmChangeEvent;
import repertuar.view.event.SeanceDayChangeEvent;

import java.util.function.Consumer;

class SeancesView extends ListView<Seance> {

    private void loadSeances(Film film) {
        setItems(FXCollections.observableList(film.getSeances()));
    }


    private void clearSeances() {
        setItems(FXCollections.emptyObservableList());
    }

    @Subscribe
    private void handleChainChangeEvent(ChainChangeEvent chainChangeEvent) {
        clearSeances();
    }

    @Subscribe
    private void handleCinemaChangeEvent(CinemaChangeEvent cinemaChangeEvent) {
        clearSeances();
    }

    @Subscribe
    private void handleSeanceDayChangeEvent(SeanceDayChangeEvent seanceDayChangeEvent) {
        clearSeances();
    }

    @Subscribe
    private void handleFilmChangeEvent(FilmChangeEvent filmChangeEvent) {
        loadSeances(filmChangeEvent.getFilm());
    }

}
