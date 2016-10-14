package repertuar.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
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
import repertuar.model.*;
import repertuar.utils.Website;
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
        this.view.addCinemasFilterTextFieldHandler(new CinemasFilterTextFieldHandler());
        this.view.addFilmsFilterTextFieldHandler(new FilmsFilterTextFieldHandler());
    }

    private ContextMenu cinemasContextMenu() {
        MenuItem visitWebsite = new MenuItem("Visit website");
        visitWebsite.setOnAction(event -> {
            try {
                Cinema cinema = view.getSelectedCinema();
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
                    Platform.runLater(view::setWaitCursor);
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
        view.clearFilmsFilterTextField();
        view.clearHoursListView();

        view.unbindTitle();
        if (cinema.getName() != null) {
            view.bindTitle(
                    cinema.cityProperty().
                            concat(" - ").
                            concat(cinema.nameProperty())
            );
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
            view.bindTitle(
                    cinema.cityProperty().
                            concat(" - ").
                            concat(cinema.nameProperty()).
                            concat(" - ").
                            concat(film.titleProperty())
            );
        } else {
            view.bindTitle(
                    cinema.cityProperty().
                            concat(" - ").
                            concat(film.titleProperty())
            );
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
                    Seance seance = view.getSelectedSeance();
                    seance.getWebsite().open();
                } catch (Exception e) {
                    handle(event);
                }
            }
        }
    }

    private class DaysHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            ComboBox<SeanceDay> days = (ComboBox<SeanceDay>) event.getSource();
            Cinema selectedCinema = view.getSelectedCinema();

            if (days != null && days.getSelectionModel().getSelectedItem() != null && selectedCinema != null) {
                int day = days.getSelectionModel().getSelectedIndex();
                String date = days.getSelectionModel().getSelectedItem().getStringToShow();
                SimpleListProperty<Film> films = days.getSelectionModel().getSelectedItem().filmsProperty();

                if (films.isEmpty()) {
                    Task task = new Task() {
                        @Override
                        protected Void call() throws Exception {
                            BorderPane pane = (BorderPane) view.getMainPane().getItems().get(1);
                            Platform.runLater(() -> setProgressIndicator(pane));
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
                view.clearFilmsFilterTextField();
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

                                setProgressIndicator(pane);
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
                view.clearCinemasFilterTextField();
                view.clearFilmsFilterTextField();
                view.bindCinemas(chain.getCinemas());
                view.clearHoursListView();
                view.clearFilmsListView();
                view.daysComboBox().getSelectionModel().clearSelection();
            }
        }
    }

    private class CinemasFilterTextFieldHandler implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            FilteredList<Cinema> filteredData = new FilteredList<>(view.getSelectedChain().getCinemas(), p -> true);
            filteredData.setPredicate(cinema -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (cinema.getName() != null && cinema.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (cinema.getCity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            view.unbindCinemas();
            view.cinemasListView().setItems(filteredData);
        }
    }

    private class FilmsFilterTextFieldHandler implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            FilteredList<Film> filteredData = new FilteredList<>(view.getSelectedDay().getFilms(), p -> true);
            filteredData.setPredicate(film -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return film.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
            view.unbindFilms();
            view.filmsListView().setItems(filteredData);
        }
    }

    private void setProgressIndicator(BorderPane pane) {
        pane.setCenter(null);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxWidth(100);
        progressIndicator.setMaxHeight(100);

        pane.setCenter(progressIndicator);
    }
}
