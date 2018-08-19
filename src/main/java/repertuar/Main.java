package repertuar;

import javafx.application.Application;
import javafx.stage.Stage;
import repertuar.view.RepertoireView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new RepertoireView(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
