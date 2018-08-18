package repertuar.view;

import javafx.scene.control.Alert;

import java.util.function.Consumer;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class ExceptionHandler implements Consumer<Exception> {

    @Override
    public void accept(Exception e) {
        Alert alert = new Alert(ERROR);

        alert.setTitle("Błąd");
        alert.setHeaderText("Wystąpił błąd podczas ładowania treści");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }

}
