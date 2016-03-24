package repertuar;

import javafx.application.Application;
import javafx.stage.Stage;
import repertuar.controller.RepertuarController;
import repertuar.model.RepertuarModel;
import repertuar.view.RepertuarView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        RepertuarView view = new RepertuarView(primaryStage);
        RepertuarModel model = new RepertuarModel();
        RepertuarController controller = new RepertuarController(view, model);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
