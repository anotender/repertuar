package repertuar.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.StringUtils;
import repertuar.controller.RepertoireController;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.SeanceDay;
import repertuar.view.event.ChainChangeEvent;
import repertuar.view.event.CinemaChangeEvent;
import repertuar.view.event.FilmChangeEvent;
import repertuar.view.event.SeanceDayChangeEvent;

import java.util.Date;
import java.util.function.Consumer;

class FilmsView extends BorderPane {

    private final EventBus eventBus;
    private final RepertoireController controller;
    private final Consumer<Exception> exceptionHandler;
    private final ComboBox<SeanceDay> seanceDays;
    private final ListView<Film> films;
    private final TextField filmFilterField;
    private Cinema currentCinema;

    FilmsView(final EventBus eventBus, final RepertoireController controller, final Consumer<Exception> exceptionHandler) {
        this.eventBus = eventBus;
        this.controller = controller;
        this.exceptionHandler = exceptionHandler;
        this.seanceDays = createSeanceDayComboBox();
        this.films = createFilmsListView();
        this.filmFilterField = createFilmFilterTextField();

        setTop(seanceDays);
        setCenter(films);
        setBottom(filmFilterField);
    }

    private ListView<Film> createFilmsListView() {
        ListView<Film> filmListView = new ListView<>();

        filmListView.setOnMouseClicked(event -> {
            Film film = films.getSelectionModel().getSelectedItem();
            if (film != null && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                eventBus.post(new FilmChangeEvent(film));
            }
        });
        filmListView.setOnKeyPressed(event -> {
            Film film = films.getSelectionModel().getSelectedItem();
            if (film != null && event.getCode().equals(KeyCode.ENTER)) {
                eventBus.post(new FilmChangeEvent(film));
            }
        });

        return filmListView;
    }

    private ComboBox<SeanceDay> createSeanceDayComboBox() {
        ComboBox<SeanceDay> seanceDayComboBox = new ComboBox<>();

        seanceDayComboBox.setMaxWidth(Double.MAX_VALUE);
        seanceDayComboBox.valueProperty().addListener((observable, oldSeanceDay, newSeanceDay) -> {
            if (newSeanceDay == null) {
                return;
            }

            Task task = new Task() {
                @Override
                protected Object call() {
                    Platform.runLater(() -> {
                        eventBus.post(new SeanceDayChangeEvent(newSeanceDay));
                        setCenter(new ProgressIndicator());
                        clearFilms();
                    });
                    loadFilms(currentCinema, newSeanceDay.getDate());
                    Platform.runLater(() -> setCenter(films));
                    return null;
                }
            };
            new Thread(task).start();
        });

        return seanceDayComboBox;
    }

    private TextField createFilmFilterTextField() {
        TextField textField = new TextField();

        textField.setPromptText("Szukaj filmu...");

        return textField;
    }

    private void loadFilms(Cinema cinema, Date date) {
        try {
            ObservableList<Film> baseList = FXCollections.observableList(controller.getFilms(cinema, date));
            FilteredList<Film> filteredList = new FilteredList<>(baseList, c -> true);
            filmFilterField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(
                            f -> StringUtils.isEmpty(newValue) ||
                                    StringUtils.containsIgnoreCase(f.getTitle(), newValue)
                    )
            );
            Platform.runLater(() -> films.setItems(filteredList));
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    private void loadSeanceDays(Cinema cinema) {
        try {
            seanceDays.setItems(FXCollections.observableList(controller.getSeanceDays(cinema)));
            seanceDays.getSelectionModel().selectFirst();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    private void clearFilms() {
        films.setItems(FXCollections.emptyObservableList());
    }

    private void clearSeanceDays() {
        seanceDays.setItems(FXCollections.emptyObservableList());
    }


    @Subscribe
    private void handleChainChangeEvent(ChainChangeEvent chainChangeEvent) {
        clearFilms();
        clearSeanceDays();
    }

    @Subscribe
    private void handleCinemaChangeEvent(CinemaChangeEvent cinemaChangeEvent) {
        currentCinema = cinemaChangeEvent.getCinema();
        clearFilms();
        clearSeanceDays();
        loadSeanceDays(currentCinema);
    }

}
