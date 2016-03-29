package repertuar.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import repertuar.model.*;
import repertuar.view.RepertuarView;

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

    }

    private ContextMenu cinemasContextMenu() {
        MenuItem visitWebsite = new MenuItem("Visit website");
        visitWebsite.setOnAction(event -> {
            try {
                Cinema cinema = (Cinema) view.cinemasListView().getSelectionModel().getSelectedItem();
                new Website(cinema.getUrl()).open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ContextMenu(visitWebsite);
    }

    private void cinemasAction() {
        Cinema cinema = view.getSelectedCinema();
        if (cinema.getDays() != null && cinema.getDays().isEmpty()) {
            Task task = new Task() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(() -> view.setWaitCursor());
                    cinema.loadDays();
                    Platform.runLater(() -> {
                        view.bindDays(cinema.getDays());
                        view.selectFirstDay();
                    });
                    return null;
                }
            };
            task.setOnCancelled(stateEvent -> view.setDefaultCursor());
            task.setOnSucceeded(stateEvent -> view.setDefaultCursor());
            task.setOnFailed(stateEvent -> view.setDefaultCursor());
            new Thread(task).start();
        }

        view.clearFilmsListView();
        view.clearHoursListView();

        view.unbindTitle();
        if (cinema.getName() != null) {
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
                Film film = view.getSelectedFilm();
                film.getWebsite().open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ContextMenu(visitWebsite);
    }

    private void filmsAction() {
        Cinema cinema = view.getSelectedCinema();
        Film film = view.getSelectedFilm();

        view.bindHours(film.getHours());
        view.unbindTitle();
        if (cinema.getName() != null) {
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
            Cinema selectedCinema = view.getSelectedCinema();

            if (days != null && days.getSelectionModel().getSelectedItem() != null && selectedCinema != null) {
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
                view.bindFilms(films);
                view.clearHoursListView();
            }
        }
    }

    private class ChainsHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Chain chain = view.getSelectedChain();
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
                view.clearHoursListView();
                view.clearFilmsListView();
                view.daysComboBox().getSelectionModel().clearSelection();
            }
        }
    }

}
