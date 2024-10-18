package clinic.cs213project3;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// For button, remember to import the JavaFX version.

/**
 * The controller class for managing what is displayed on the JavaFX UI.
 * @author Aarush Desai
 */
public class ClinicManagerController {

    //@FXML
    //private Label welcomeText;

    //@FXML
    //protected void onHelloButtonClick() {
    //    welcomeText.setText("Welcome to the Clinic Manager!");
    //}

    /**
     * Commands...
     *     * D
     *         ** Schedule with Doctor.
     *     * T
     *         ** Scheduler with Technician.
     *     * C
     *         ** Cancel appointment.
     *     * R
     *         ** Reschedule appointment.
     *     * PA
     *         ** Print appointments by date.
     *     * PP
     *         ** Print appointments by patient.
     *     * PL
     *         ** Print appointments by county.
     *     * PO
     *         ** Print office appointments by county.
     *     * PI
     *         ** Print imaging appointments by county.
     *     * PS
     *         ** Print patient billing.
     *     * PC
     *         ** Print provider credits.
     *     * Q
     *         ** Quit program.
     *
     * JavaFX Utilities...
     *     * TextField [^]
     *         * Def: Allow single-line user input.
     *     * Button [^]
     *         * Def: A clickable control.
     *     * RadioButton [^]
     *         * Def: Checkbox selection from a bunch of choices.
     *     * TextArea [^]
     *         * Def: Allow multiple-line user input.
     *     * TableView [^]
     *         * Def: Display data in a table.
     *     * TabPane [^]
     *         * Def: Organize content into tabs.
     *     * GridPane [^]
     *         * Def: Allows placement of UI elements in grid.
     *
     * Structure...
     *     * Schedule Button (TabPane)
     *         ** Office Appointment (RadioButton)
     *             *** Inputs (TableView)
     *         ** ImagingAppointment (RadioButton)
     *             *** Inputs (TableView)
     *     * Cancel Button (TabPane)
     *         ** Inputs (TableView)
     *     * Reschedule Button (TabPane)
     *         ** Inputs (TableView)
     *     * Appointments Button (TabPane)
     *         ** Print Office Appointments (TabPane)
     *         ** Print Imaging Appointments (TabPane)
     *         ** Print All Appointments (TabPane)
     *             *** PA (TabPane)
     *             *** PP (TabPane)
     *             *** PL (TabPane)
     *     * Billing Button (TabPane)
     *         ** Patient Billing (TabPane)
     *         ** Provider Credits (TabPane)
     */

}