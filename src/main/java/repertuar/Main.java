package repertuar;

import javafx.application.Application;
import javafx.stage.Stage;
import repertuar.controller.RepertoireController;
import repertuar.view.RepertoireView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        RepertoireController controller = new RepertoireController();
        RepertoireView view = new RepertoireView(primaryStage, controller);

    }


    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF); /* comment out to turn off annoying htmlunit warnings */
        launch(args);
    }
}
