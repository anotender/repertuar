package repertuar.view;

import com.google.common.eventbus.EventBus;
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
import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.view.event.ChainChangeEvent;
import repertuar.view.event.CinemaChangeEvent;

import java.util.Arrays;
import java.util.function.Consumer;

class CinemasView extends BorderPane {

    private final EventBus eventBus;
    private final RepertoireController controller;
    private final Consumer<Exception> exceptionHandler;
    private final ComboBox<Chain> chains;
    private final ListView<Cinema> cinemas;
    private final TextField cinemaFilterField;

    CinemasView(final EventBus eventBus, final RepertoireController controller, final Consumer<Exception> exceptionHandler) {
        super();
        this.eventBus = eventBus;
        this.controller = controller;
        this.exceptionHandler = exceptionHandler;
        this.chains = createChainsComboBox();
        this.cinemas = createCinemasListView();
        this.cinemaFilterField = createCinemaFilterTextField();

        setTop(chains);
        setCenter(cinemas);
        setBottom(cinemaFilterField);
    }

    private ComboBox<Chain> createChainsComboBox() {
        ComboBox<Chain> chainComboBox = new ComboBox<>();

        chainComboBox.setItems(FXCollections.observableList(Arrays.asList(Chain.values())));
        chainComboBox.setMaxWidth(Double.MAX_VALUE);
        chainComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Task task = new Task() {
                @Override
                protected Object call() {
                    Platform.runLater(() -> {
                        setCenter(new ProgressIndicator());
                        eventBus.post(new ChainChangeEvent(newValue));
                    });
                    loadCinemas(newValue);
                    Platform.runLater(() -> setCenter(cinemas));
                    return null;
                }
            };
            new Thread(task).start();
        });

        return chainComboBox;
    }

    private ListView<Cinema> createCinemasListView() {
        ListView<Cinema> cinemaListView = new ListView<>();

        cinemaListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                eventBus.post(new CinemaChangeEvent(cinemaListView.getSelectionModel().getSelectedItem()));
            }
        });
        cinemaListView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                eventBus.post(new CinemaChangeEvent(cinemaListView.getSelectionModel().getSelectedItem()));
            }
        });

        return cinemaListView;
    }

    private TextField createCinemaFilterTextField() {
        TextField textField = new TextField();

        textField.setPromptText("Szukaj kina...");

        return textField;
    }

    private void loadCinemas(Chain chain) {
        try {
            ObservableList<Cinema> baseList = FXCollections.observableList(chain.getCinemas());
            FilteredList<Cinema> filteredList = new FilteredList<>(baseList, c -> true);
            cinemaFilterField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(c ->
                            StringUtils.isEmpty(newValue) ||
                                    StringUtils.containsIgnoreCase(c.getName(), newValue)
                    )
            );
            Platform.runLater(() -> cinemas.setItems(filteredList));
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

}
