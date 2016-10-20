package repertuar.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repertuar.controller.RepertuarController;
import repertuar.model.*;

import java.util.Date;

public class RepertuarView {

    private Stage primaryStage;
    private RepertuarController controller;
    private BorderPane chainAndCinemasPane = new BorderPane();
    private BorderPane seanceDayAndFilmsPane = new BorderPane();
    private ComboBox<Chain> chains = new ComboBox<>();
    private ComboBox<SeanceDay> seanceDays = new ComboBox<>();
    private ListView<Cinema> cinemas = new ListView();
    private ListView<Film> films = new ListView();
    private ListView<Seance> seances = new ListView();

    public RepertuarView(Stage primaryStage, RepertuarController controller) {
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
        chainAndCinemasPane.setTop(chains);
        chainAndCinemasPane.setCenter(cinemas);
        chainAndCinemasPane.setBottom(new TextField());

        prepareSeanceDaysComboBox();
        films.setOnMouseClicked(event -> {
            Film film = films.getSelectionModel().getSelectedItem();
            if (film != null && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                loadSeances(film);
            }
        });
        seanceDayAndFilmsPane.setTop(seanceDays);
        seanceDayAndFilmsPane.setCenter(films);
        seanceDayAndFilmsPane.setBottom(new TextField());

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
                protected Object call() throws Exception {
                    Platform.runLater(() -> {
                        chainAndCinemasPane.setCenter(new ProgressIndicator());
                        clearFilms();
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
                    protected Object call() throws Exception {
                        Platform.runLater(() -> seanceDayAndFilmsPane.setCenter(new ProgressIndicator()));
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
            cinemas.setItems(FXCollections.observableList(controller.getCinemas(chain)));
        } catch (Exception e) {
            showErrorInfo(e);
            e.printStackTrace();
        }
    }

    private void loadSeanceDays(Cinema cinema) {
        try {
            seanceDays.setItems(FXCollections.observableList(controller.getSeanceDays(cinema)));
            seanceDays.getSelectionModel().selectFirst();
        } catch (Exception e) {
            showErrorInfo(e);
            e.printStackTrace();
        }
    }

    private void loadFilms(Cinema cinema, Date date) {
        try {
            films.setItems(FXCollections.observableList(controller.getFilms(cinema, date)));
        } catch (Exception e) {
            showErrorInfo(e);
            e.printStackTrace();
        }
    }

    private void loadSeances(Film film) {
        seances.setItems(FXCollections.observableList(film.getSeances()));
    }

    private void clearFilms() {
        films.getItems().clear();
    }

    private void clearSeanceDays() {
        seanceDays.getItems().clear();
    }

    private void clearSeances() {
        seances.getItems().clear();
    }

    private void showErrorInfo(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Wystąpił błąd podczas ładowania treści");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }
}
