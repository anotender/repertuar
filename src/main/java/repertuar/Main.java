package repertuar;

import javafx.application.Application;
import javafx.stage.Stage;
import repertuar.controller.RepertuarController;
import repertuar.view.RepertuarView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        RepertuarController controller = new RepertuarController();
        RepertuarView view = new RepertuarView(primaryStage, controller);

    }


    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF); /* comment out to turn off annoying htmlunit warnings */
        launch(args);
    }
}
