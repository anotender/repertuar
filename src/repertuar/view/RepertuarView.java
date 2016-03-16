package repertuar.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import repertuar.model.Chain;
import repertuar.model.Website;
import repertuar.model.Cinema;
import repertuar.model.Film;

import java.util.List;

/**
 * Created by mateu on 22.09.2015.
 */
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

        this.root = new BorderPane();
        this.mainPane = new SplitPane();
        this.primaryStage = primaryStage;
        this.chains = new ComboBox<>();
        this.chains.setMaxWidth(Double.MAX_VALUE);
        this.cinemas = new ListView();
        this.films = new ListView();
        this.hours = new ListView();
        this.days = new ComboBox<>();
        this.days.setMaxWidth(Double.MAX_VALUE);

        this.cinemas.setCellFactory(new Callback<ListView<Cinema>, ListCell<Cinema>>() {
            @Override
            public ListCell<Cinema> call(ListView<Cinema> param) {
                ListCell<Cinema> cell = new ListCell<Cinema>() {
                    @Override
                    protected void updateItem(Cinema item, boolean b) {
                        super.updateItem(item, b);
                        if (item != null) {
                            if (item.nameProperty().get() != null) {
                                textProperty().bind(item.cityProperty().concat(" - ").concat(item.nameProperty()));
                            } else {
                                textProperty().bind(item.cityProperty());
                            }
                        } else {
                            textProperty().unbind();
                            textProperty().set("");
                        }
                    }
                };

                return cell;
            }
        });

        this.films.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            @Override
            public ListCell<Film> call(ListView<Film> param) {
                ListCell<Film> cell = new ListCell<Film>() {
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

                return cell;
            }
        });

        this.hours.setCellFactory(new Callback<ListView<Pair<SimpleStringProperty, Website>>, ListCell<Pair<SimpleStringProperty, Website>>>() {
            @Override
            public ListCell<Pair<SimpleStringProperty, Website>> call(ListView<Pair<SimpleStringProperty, Website>> param) {
                ListCell<Pair<SimpleStringProperty, Website>> cell = new ListCell<Pair<SimpleStringProperty, Website>>() {
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

                return cell;
            }
        });

        this.days.setButtonCell(new ListCell<Pair<String, SimpleListProperty<Film>>>() {
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

        this.days.setCellFactory(new Callback<ListView<Pair<String, SimpleListProperty<Film>>>, ListCell<Pair<String, SimpleListProperty<Film>>>>() {
            @Override
            public ListCell<Pair<String, SimpleListProperty<Film>>> call(ListView<Pair<String, SimpleListProperty<Film>>> param) {
                ListCell<Pair<String, SimpleListProperty<Film>>> cell = new ListCell<Pair<String, SimpleListProperty<Film>>>() {
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

                return cell;
            }
        });

        mainPane.getItems().add(new BorderPane(this.cinemas, this.chains, null, null, null));
        mainPane.getItems().add(new BorderPane(this.films, this.days, null, null, null));
        mainPane.getItems().add(this.hours);
        mainPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);

        this.root.setCenter(mainPane);

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

//    public void unbindCinemas() {
//        cinemas.itemsProperty().unbind();
//    }

    public void bindFilms(SimpleListProperty<Film> list) {
        films.itemsProperty().bind(list);
    }

//    public void unbindFilms() {
//        films.itemsProperty().unbind();
//    }

    public void bindHours(SimpleListProperty<Pair<SimpleStringProperty, Website>> list) {
        hours.itemsProperty().bind(list);
    }

//    public void unbindHours() {
//        hours.itemsProperty().unbind();
//    }

    public void bindDays(SimpleListProperty list) {
        days.itemsProperty().bind(list);
    }

    public void selectFirstDay() {
        days.getSelectionModel().selectFirst();
    }

    public void addMenuBar(MenuBar menuBar) {
        root.setTop(menuBar);
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
