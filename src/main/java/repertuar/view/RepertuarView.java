package repertuar.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import repertuar.model.*;

public class RepertuarView {

    private final ComboBox<Chain> chains;
    private final ListView<Cinema> cinemas;
    private final TextField cinemasFilterTextField;
    private final ListView<Film> films;
    private final TextField filmsFilterTextField;
    private final ListView<Seance> seances;
    private final ComboBox<SeanceDay> seanceDays;
    private final BorderPane root;
    private final SplitPane mainPane;
    private final Stage primaryStage;

    public RepertuarView(Stage primaryStage) {

        this.primaryStage = primaryStage;
        root = new BorderPane();
        mainPane = new SplitPane();
        chains = new ComboBox<>();
        chains.setMaxWidth(Double.MAX_VALUE);
        cinemas = new ListView<>();
        cinemasFilterTextField = new TextField();
        cinemasFilterTextField.setPromptText("Search for cinema");
        films = new ListView<>();
        filmsFilterTextField = new TextField();
        filmsFilterTextField.setPromptText("Search for film");
        seances = new ListView<>();
        seanceDays = new ComboBox<>();
        seanceDays.setMaxWidth(Double.MAX_VALUE);

        cinemas.setCellFactory(new Callback<ListView<Cinema>, ListCell<Cinema>>() {
            @Override
            public ListCell<Cinema> call(ListView<Cinema> param) {
                return new ListCell<Cinema>() {
                    @Override
                    protected void updateItem(Cinema cinema, boolean b) {
                        super.updateItem(cinema, b);
                        if (cinema != null) {
                            if (cinema.getName() != null) {
                                textProperty().bind(cinema.cityProperty().concat(" - ").concat(cinema.nameProperty()));
                            } else {
                                textProperty().bind(cinema.cityProperty());
                            }
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        films.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            @Override
            public ListCell<Film> call(ListView<Film> param) {
                return new ListCell<Film>() {
                    @Override
                    protected void updateItem(Film item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            textProperty().bind(item.titleProperty());
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        seances.setCellFactory(new Callback<ListView<Seance>, ListCell<Seance>>() {
            @Override
            public ListCell<Seance> call(ListView<Seance> param) {
                return new ListCell<Seance>() {
                    @Override
                    protected void updateItem(Seance item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            textProperty().bind(item.stringToShowProperty());
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        seanceDays.setButtonCell(new ListCell<SeanceDay>() {
            @Override
            protected void updateItem(SeanceDay item, boolean b) {
                super.updateItem(item, b);
                if (item != null) {
                    String stringToShow = item.getStringToShow();
                    if (stringToShow.contains(":")) {
                        textProperty().bind(new SimpleStringProperty(stringToShow.substring(0, stringToShow.indexOf(":"))));
                    } else {
                        textProperty().bind(new SimpleStringProperty(stringToShow));
                    }
                } else {
                    textProperty().unbind();
                    textProperty().set("");
                }
            }
        });

        seanceDays.setCellFactory(new Callback<ListView<SeanceDay>, ListCell<SeanceDay>>() {
            @Override
            public ListCell<SeanceDay> call(ListView<SeanceDay> param) {
                return new ListCell<SeanceDay>() {
                    @Override
                    protected void updateItem(SeanceDay item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            String stringToShow = item.getStringToShow();
                            if (stringToShow.contains(":")) {
                                textProperty().bind(new SimpleStringProperty(stringToShow.substring(0, stringToShow.indexOf(":"))));
                            } else {
                                textProperty().bind(new SimpleStringProperty(stringToShow));
                            }
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        mainPane.getItems().add(new BorderPane(cinemas, chains, null, cinemasFilterTextField, null));
        mainPane.getItems().add(new BorderPane(films, seanceDays, null, filmsFilterTextField, null));
        mainPane.getItems().add(seances);
        mainPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);

        root.setCenter(mainPane);

        Scene scene = new Scene(root, 1024, 768);

        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Repertuar");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();

    }

    public void bindTitle(ObservableValue<? extends String> observable) {
        primaryStage.titleProperty().bind(observable);
    }

    public void unbindTitle() {
        primaryStage.titleProperty().unbind();
    }

    public void bindChains(SimpleListProperty<Chain> list) {
        chains.itemsProperty().bind(list);
    }

    public void bindCinemas(ObservableValue<? extends ObservableList<Cinema>> list) {
        cinemas.itemsProperty().bind(list);
    }

    public void unbindCinemas() {
        cinemas.itemsProperty().unbind();
    }

    public void bindFilms(SimpleListProperty<Film> list) {
        films.itemsProperty().bind(list);
    }

    public void unbindFilms() {
        films.itemsProperty().unbind();
    }

    public void bindHours(SimpleListProperty<Seance> list) {
        seances.itemsProperty().bind(list);
    }

    public void bindDays(SimpleListProperty<SeanceDay> list) {
        seanceDays.itemsProperty().bind(list);
    }

    public void selectFirstDay() {
        seanceDays.getSelectionModel().selectFirst();
    }

    public void addChainsHandler(EventHandler<ActionEvent> handler) {
        chains.setOnAction(handler);
    }

    public void addCinemasHandler(EventHandler<MouseEvent> mouseHandler, EventHandler<KeyEvent> keyHandler) {
        cinemas.setOnMouseClicked(mouseHandler);
        cinemas.setOnKeyPressed(keyHandler);
    }

    public void addCinemasContextMenu(ContextMenu contextMenu) {
        cinemas.setContextMenu(contextMenu);
    }

    public void addFilmsHandler(EventHandler<MouseEvent> mouseHandler, EventHandler<KeyEvent> keyHandler) {
        films.setOnMouseClicked(mouseHandler);
        films.setOnKeyPressed(keyHandler);
    }

    public void addFilmsContextMenu(ContextMenu contextMenu) {
        films.setContextMenu(contextMenu);
    }

    public void addHoursHandler(EventHandler<MouseEvent> handler) {
        seances.setOnMouseClicked(handler);
    }

    public void addDaysHandler(EventHandler<ActionEvent> handler) {
        seanceDays.setOnAction(handler);
    }

    public void addCinemasFilterTextFieldHandler(ChangeListener<String> changeListener) {
        cinemasFilterTextField.textProperty().addListener(changeListener);
    }

    public void addFilmsFilterTextFieldHandler(ChangeListener<String> changeListener) {
        filmsFilterTextField.textProperty().addListener(changeListener);
    }

    public ListView cinemasListView() {
        return cinemas;
    }

    public ListView filmsListView() {
        return films;
    }

    public ComboBox daysComboBox() {
        return seanceDays;
    }

    public ListView hoursListView() {
        return seances;
    }

    public SplitPane getMainPane() {
        return mainPane;
    }

    public void clearFilmsListView() {
        films.itemsProperty().unbind();
        films.setItems(null);
    }

    public void clearHoursListView() {
        seances.itemsProperty().unbind();
        seances.setItems(null);
    }

    public void clearCinemasFilterTextField() {
        cinemasFilterTextField.clear();
    }

    public void clearFilmsFilterTextField() {
        filmsFilterTextField.clear();
    }

    public Chain getSelectedChain() {
        return chains.getSelectionModel().getSelectedItem();
    }

    public Cinema getSelectedCinema() {
        return cinemas.getSelectionModel().getSelectedItem();
    }

    public SeanceDay getSelectedDay() {
        return seanceDays.getSelectionModel().getSelectedItem();
    }

    public Film getSelectedFilm() {
        return films.getSelectionModel().getSelectedItem();
    }

    public void setDefaultCursor() {
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
    }

    public void setWaitCursor() {
        primaryStage.getScene().setCursor(Cursor.WAIT);
    }
}
