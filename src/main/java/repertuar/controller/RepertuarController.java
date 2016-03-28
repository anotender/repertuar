package repertuar.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import repertuar.model.*;
import repertuar.model.helios.HeliosCinema;
import repertuar.view.RepertuarView;

import java.io.File;
import java.io.PrintWriter;

public class RepertuarController {

    private final RepertuarView view;
    private final RepertuarModel model;

    public RepertuarController(RepertuarView view, RepertuarModel model) {

        this.view = view;
        this.model = model;

        this.view.bindChains(this.model.getChains());
        this.view.addChainsHandler(new ChainsHandler());
        this.view.addCinemasHandler(new CinemasMouseHandler(), new CinemasKeyHandler());
        this.view.addCinemasContextMenu(cinemasContextMenu());
        this.view.addFilmsHandler(new FilmsMouseHandler(), new FilmsKeyHandler());
        this.view.addFilmsContextMenu(filmsContextMenu());
        this.view.addHoursHandler(new HoursHandler());
        this.view.addDaysHandler(new DaysHandler());
        this.view.addMenuBar(createMenuBar());

    }

    private MenuBar createMenuBar() {
        MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> {
            HeliosCinema selectedCinema = (HeliosCinema) view.cinemasListView().getSelectionModel().getSelectedItem();
            Film selectedFilm = (Film) view.filmsListView().getSelectionModel().getSelectedItem();

            if (selectedCinema == null || selectedFilm == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Wybierz kino i film");
                alert.showAndWait();

                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("repertuar");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt"));
            File file = fileChooser.showSaveDialog(view.getPrimaryStage());

            try {
                PrintWriter out = new PrintWriter(file);
                out.println(selectedCinema.cityProperty().get() + " - " + selectedCinema.nameProperty().get());
                out.println(selectedFilm.titleProperty().get());

//                for (Pair<SimpleStringProperty, SimpleListProperty<Pair<SimpleStringProperty, Website>>> day : selectedFilm.getHours()) {
//                    out.println();
//                    out.println(day.getKey().get());
//
//                    for (Pair<SimpleStringProperty, Website> hour : day.getValue()) {
//                        out.println(hour.getKey().get());
//                    }
//                }

                out.close();
            } catch (Exception e) {
                System.out.println("Exception");
            }
        });

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(save);

        return new MenuBar(fileMenu);
    }

    private ContextMenu cinemasContextMenu() {
        MenuItem visitWebsite = new MenuItem("Visit website");
        visitWebsite.setOnAction(event -> {
            try {
                HeliosCinema cinema = (HeliosCinema) view.cinemasListView().getSelectionModel().getSelectedItem();
                new Website(cinema.urlProperty().get()).open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ContextMenu(visitWebsite);
    }

    private void cinemasAction() {
//        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

        Cinema cinema = (Cinema) view.cinemasListView().getSelectionModel().getSelectedItem();
        if (cinema.getDays().isEmpty()) {
            Task task = new Task() {
                @Override
                protected Void call() throws Exception {
                    RepertuarController.this.view.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
                    cinema.loadDays();
                    Platform.runLater(() -> view.daysComboBox().getSelectionModel().select(0));
                    return null;
                }
            };
            task.setOnCancelled(stateEvent -> view.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            task.setOnSucceeded(stateEvent -> view.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            task.setOnFailed(stateEvent -> view.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            new Thread(task).start();
        }
        view.filmsListView().itemsProperty().unbind();
        view.filmsListView().setItems(null);

        view.hoursListView().itemsProperty().unbind();
        view.hoursListView().setItems(null);

        view.bindDays(cinema.getDays());
        view.selectFirstDay();
        view.unbindTitle();
        if (cinema.nameProperty().get() != null) {
            view.bindTitle(cinema.cityProperty().concat(" - ").concat(cinema.nameProperty()));
        } else {
            view.bindTitle(cinema.cityProperty());
        }

    }

    private class CinemasMouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                cinemasAction();
            }
        }
    }

    private class CinemasKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                cinemasAction();
            }
        }
    }

    private ContextMenu filmsContextMenu() {
        MenuItem visitWebsite = new MenuItem("Visit website");
        visitWebsite.setOnAction(event -> {
            try {
                Film film = (Film) view.filmsListView().getSelectionModel().getSelectedItem();
                film.getWebsite().open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ContextMenu(visitWebsite);
    }

    private void filmsAction() {
        Cinema cinema = (Cinema) view.cinemasListView().getSelectionModel().getSelectedItem();
        Film film = (Film) view.filmsListView().getSelectionModel().getSelectedItem();

        view.bindHours(film.getHours());
        view.unbindTitle();
        if (cinema.nameProperty().get() != null) {
            view.bindTitle(cinema.cityProperty().concat(" - ").concat(cinema.nameProperty()).concat(" - ").concat(film.titleProperty()));
        } else {
            view.bindTitle(cinema.cityProperty().concat(" - ").concat(film.titleProperty()));
        }
    }

    private class FilmsMouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                filmsAction();
            }
        }
    }

    private class FilmsKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                filmsAction();
            }
        }
    }

    private class HoursHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                try {
                    ((Pair<SimpleStringProperty, Website>) view.hoursListView().getSelectionModel().getSelectedItem()).getValue().open();
                } catch (Exception e) {
                    handle(event);
                }
            }
        }
    }

    private class DaysHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ComboBox<Pair<String, SimpleListProperty<Film>>> days = (ComboBox<Pair<String, SimpleListProperty<Film>>>) event.getSource();
            Cinema selectedCinema = (Cinema) view.cinemasListView().getSelectionModel().getSelectedItem();

            //if (days.getSelectionModel().getSelectedItem() != null) return;

            if (days != null && selectedCinema != null) {
                int day = days.getSelectionModel().getSelectedIndex();
                String date = days.getSelectionModel().getSelectedItem().getKey();
                SimpleListProperty<Film> films = days.getSelectionModel().getSelectedItem().getValue();

                if (films.isEmpty()) {
                    Task task = new Task() {
                        @Override
                        protected Void call() throws Exception {
                            BorderPane pane = (BorderPane) view.getMainPane().getItems().get(1);
                            Platform.runLater(() -> {
                                pane.setCenter(null);

                                ProgressIndicator progressIndicator = new ProgressIndicator();
                                progressIndicator.setMaxWidth(100);
                                progressIndicator.setMaxHeight(100);

                                pane.setCenter(progressIndicator);
                            });
                            selectedCinema.loadFilms(day, date);
                            Platform.runLater(() -> {
                                pane.setCenter(null);
                                pane.setCenter(view.filmsListView());
                            });
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
                view.filmsListView().itemsProperty().bind(films);

                view.hoursListView().itemsProperty().unbind();
                view.hoursListView().setItems(null);
            }
        }
    }

    private class ChainsHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Chain chain = ((ComboBox<Chain>) event.getSource()).getSelectionModel().getSelectedItem();
            if (chain != null) {
                if (chain.getCinemas().isEmpty()) {
                    Task task = new Task() {
                        @Override
                        protected Void call() throws Exception {
                            BorderPane pane = (BorderPane) view.getMainPane().getItems().get(0);
                            Platform.runLater(() -> {
                                view.daysComboBox().getSelectionModel().clearSelection();
                                view.daysComboBox().getItems().clear();

                                pane.setCenter(null);

                                ProgressIndicator progressIndicator = new ProgressIndicator();
                                progressIndicator.setMaxWidth(100);
                                progressIndicator.setMaxHeight(100);

                                pane.setCenter(progressIndicator);
                            });
                            chain.loadCinemas();
                            Platform.runLater(() -> {
                                pane.setCenter(null);
                                pane.setCenter(view.cinemasListView());
                            });
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
                view.bindCinemas(chain.getCinemas());

                view.hoursListView().itemsProperty().unbind();
                view.hoursListView().setItems(null);

                view.filmsListView().itemsProperty().unbind();
                view.filmsListView().setItems(null);
            }
        }
    }

}
