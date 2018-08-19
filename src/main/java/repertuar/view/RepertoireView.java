package repertuar.view;

import com.google.common.eventbus.EventBus;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repertuar.model.Seance;

import java.util.function.Consumer;

public class RepertoireView {

    public RepertoireView(Stage primaryStage) {
        EventBus eventBus = new EventBus();

        Consumer<Exception> exceptionHandler = new ExceptionHandler();
        BorderPane cinemasView = new CinemasView(eventBus, exceptionHandler);
        BorderPane filmsView = new FilmsView(eventBus, exceptionHandler);
        ListView<Seance> seancesView = new SeancesView();

        eventBus.register(filmsView);
        eventBus.register(seancesView);

        SplitPane splitPane = new SplitPane(cinemasView, filmsView, seancesView);
        splitPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);

        Scene scene = new Scene(splitPane, 1024, 768);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Repertuar");
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(480);
        primaryStage.show();
    }

}
