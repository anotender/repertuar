package repertuar.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Pair;
import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.Film;
import repertuar.model.Website;

public class RepertuarView {

    private final ComboBox<Chain> chains;
    private final ListView<Cinema> cinemas;
    private final ListView<Film> films;
    private final ListView<Pair<SimpleStringProperty, Website>> hours;
    private final ComboBox<Pair<String, SimpleListProperty<Film>>> days;
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
        films = new ListView<>();
        hours = new ListView<>();
        days = new ComboBox<>();
        days.setMaxWidth(Double.MAX_VALUE);

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

        hours.setCellFactory(new Callback<ListView<Pair<SimpleStringProperty, Website>>, ListCell<Pair<SimpleStringProperty, Website>>>() {
            @Override
            public ListCell<Pair<SimpleStringProperty, Website>> call(ListView<Pair<SimpleStringProperty, Website>> param) {
                return new ListCell<Pair<SimpleStringProperty, Website>>() {
                    @Override
                    protected void updateItem(Pair<SimpleStringProperty, Website> item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            textProperty().bind(item.getKey());
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        days.setButtonCell(new ListCell<Pair<String, SimpleListProperty<Film>>>() {
            @Override
            protected void updateItem(Pair<String, SimpleListProperty<Film>> item, boolean b) {
                super.updateItem(item, b);
                if (item != null) {
                    if (item.getKey().contains(":")) {
                        textProperty().bind(new SimpleStringProperty(item.getKey().substring(0, item.getKey().indexOf(":"))));
                    } else {
                        textProperty().bind(new SimpleStringProperty(item.getKey()));
                    }
                } else {
                    textProperty().unbind();
                    textProperty().set("");
                }
            }
        });

        days.setCellFactory(new Callback<ListView<Pair<String, SimpleListProperty<Film>>>, ListCell<Pair<String, SimpleListProperty<Film>>>>() {
            @Override
            public ListCell<Pair<String, SimpleListProperty<Film>>> call(ListView<Pair<String, SimpleListProperty<Film>>> param) {
                return new ListCell<Pair<String, SimpleListProperty<Film>>>() {
                    @Override
                    protected void updateItem(Pair<String, SimpleListProperty<Film>> item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            if (item.getKey().contains(":")) {
                                textProperty().bind(new SimpleStringProperty(item.getKey().substring(0, item.getKey().indexOf(":"))));
                            } else {
                                textProperty().bind(new SimpleStringProperty(item.getKey()));
                            }
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };
            }
        });

        mainPane.getItems().add(new BorderPane(cinemas, chains, null, null, null));
        mainPane.getItems().add(new BorderPane(films, days, null, null, null));
        mainPane.getItems().add(this.hours);
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

    public void bindCinemas(SimpleListProperty<Cinema> list) {
        cinemas.itemsProperty().bind(list);
    }

    public void bindFilms(SimpleListProperty<Film> list) {
        films.itemsProperty().bind(list);
    }

    public void bindHours(SimpleListProperty<Pair<SimpleStringProperty, Website>> list) {
        hours.itemsProperty().bind(list);
    }

    public void bindDays(SimpleListProperty list) {
        days.itemsProperty().bind(list);
    }

    public void selectFirstDay() {
        days.getSelectionModel().selectFirst();
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
        hours.setOnMouseClicked(handler);
    }

    public void addDaysHandler(EventHandler<ActionEvent> handler) {
        days.setOnAction(handler);
    }

    public ListView cinemasListView() {
        return cinemas;
    }

    public ListView filmsListView() {
        return films;
    }

    public ComboBox daysComboBox() {
        return days;
    }

    public ListView hoursListView() {
        return hours;
    }

    public SplitPane getMainPane() {
        return mainPane;
    }

    public void clearFilmsListView() {
        films.itemsProperty().unbind();
        films.setItems(null);
    }

    public void clearHoursListView() {
        hours.itemsProperty().unbind();
        hours.setItems(null);
    }

    public Chain getSelectedChain() {
        return chains.getSelectionModel().getSelectedItem();
    }

    public Cinema getSelectedCinema() {
        return cinemas.getSelectionModel().getSelectedItem();
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
