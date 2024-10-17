package clinic.cs213project3;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The controller class for managing what is displayed on the JavaFX UI.
 * @author Aarush Desai
 */
public class ClinicManagerController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void tryButtonClick() {
        welcomeText.setText("This is a test button!");
    }

}