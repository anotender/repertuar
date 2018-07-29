package repertuar.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import repertuar.controller.RepertoireController;
import repertuar.model.*;

import java.util.Date;

public class RepertoireView {

    private Stage primaryStage;
    private RepertoireController controller;
    private BorderPane chainAndCinemasPane = new BorderPane();
    private BorderPane seanceDayAndFilmsPane = new BorderPane();
    private ComboBox<Chain> chains = new ComboBox<>();
    private ComboBox<SeanceDay> seanceDays = new ComboBox<>();
    private ListView<Cinema> cinemas = new ListView<>();
    private ListView<Film> films = new ListView<>();
    private ListView<Seance> seances = new ListView<>();
    private TextField cinemaFilterField = new TextField();
    private TextField filmFilterField = new TextField();

    public RepertoireView(Stage primaryStage, RepertoireController controller) {
        this.primaryStage = primaryStage;
        this.controller = controller;

        prepareChainsComboBox();
        cinemas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                clearSeanceDays();
                clearSeances();
                loadSeanceDays(cinemas.getSelectionModel().getSelectedItem());
            }
        });
        cinemas.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                clearSeanceDays();
                clearSeances();
                loadSeanceDays(cinemas.getSelectionModel().getSelectedItem());
            }
        });
        cinemaFilterField.setPromptText("Szukaj kina...");
        chainAndCinemasPane.setTop(chains);
        chainAndCinemasPane.setCenter(cinemas);
        chainAndCinemasPane.setBottom(cinemaFilterField);

        prepareSeanceDaysComboBox();
        films.setOnMouseClicked(event -> {
            Film film = films.getSelectionModel().getSelectedItem();
            if (film != null && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                loadSeances(film);
            }
        });
        films.setOnKeyPressed(event -> {
            Film film = films.getSelectionModel().getSelectedItem();
            if (film != null && event.getCode().equals(KeyCode.ENTER)) {
                loadSeances(film);
            }
        });
        filmFilterField.setPromptText("Szukaj filmu...");
        seanceDayAndFilmsPane.setTop(seanceDays);
        seanceDayAndFilmsPane.setCenter(films);
        seanceDayAndFilmsPane.setBottom(filmFilterField);

        SplitPane splitPane = new SplitPane(chainAndCinemasPane, seanceDayAndFilmsPane, seances);
        splitPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);

        Scene scene = new Scene(splitPane, 1024, 768);

        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Repertuar");
        this.primaryStage.setMinWidth(640);
        this.primaryStage.setMinHeight(480);
        this.primaryStage.show();
    }

    private void prepareChainsComboBox() {
        chains.setItems(FXCollections.observableList(controller.getChains()));
        chains.setMaxWidth(Double.MAX_VALUE);
        chains.valueProperty().addListener((observable, oldValue, newValue) -> {
            Task task = new Task() {
                @Override
                protected Object call() {
                    Platform.runLater(() -> {
                        chainAndCinemasPane.setCenter(new ProgressIndicator());
                        clearFilms();
                        clearSeances();
                        clearSeanceDays();
                    });
                    loadCinemas(newValue);
                    Platform.runLater(() -> chainAndCinemasPane.setCenter(cinemas));
                    return null;
                }
            };
            new Thread(task).start();
        });
    }

    private void prepareSeanceDaysComboBox() {
        seanceDays.setMaxWidth(Double.MAX_VALUE);
        seanceDays.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Task task = new Task() {
                    @Override
                    protected Object call() {
                        Platform.runLater(() -> {
                            seanceDayAndFilmsPane.setCenter(new ProgressIndicator());
                            clearSeances();
                            clearFilms();
                        });
                        Cinema cinema = cinemas.getSelectionModel().getSelectedItem();
                        loadFilms(cinema, newValue.getDate());
                        Platform.runLater(() -> seanceDayAndFilmsPane.setCenter(films));
                        return null;
                    }
                };
                new Thread(task).start();
            }
        });
    }

    private void loadCinemas(Chain chain) {
        try {
            ObservableList<Cinema> baseList = FXCollections.observableList(controller.getCinemas(chain));
            FilteredList<Cinema> filteredList = new FilteredList<>(baseList, c -> true);
            cinemaFilterField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(c ->
                            StringUtils.isEmpty(newValue) ||
                                    StringUtils.containsIgnoreCase(c.getName(), newValue)
                    )
            );
            Platform.runLater(() -> cinemas.setItems(filteredList));
        } catch (Exception e) {
            showErrorInfo(e);
        }
    }

    private void loadSeanceDays(Cinema cinema) {
        try {
            seanceDays.setItems(FXCollections.observableList(controller.getSeanceDays(cinema)));
            seanceDays.getSelectionModel().selectFirst();
        } catch (Exception e) {
            showErrorInfo(e);
        }
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
            showErrorInfo(e);
        }
    }

    private void loadSeances(Film film) {
        seances.setItems(FXCollections.observableList(film.getSeances()));
    }

    private void clearFilms() {
        films.setItems(FXCollections.emptyObservableList());
    }

    private void clearSeanceDays() {
        seanceDays.setItems(FXCollections.emptyObservableList());
    }

    private void clearSeances() {
        seances.setItems(FXCollections.emptyObservableList());
    }

    private void showErrorInfo(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Wystąpił błąd podczas ładowania treści");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }
}
