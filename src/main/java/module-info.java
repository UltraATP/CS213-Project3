module clinic.cs213project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens clinic.cs213project3 to javafx.fxml;
    exports clinic.cs213project3;
}