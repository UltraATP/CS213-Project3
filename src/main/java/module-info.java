module clinic.cs213project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens clinic.cs213project3 to javafx.fxml;
    exports clinic.cs213project3;
}