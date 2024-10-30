module clinic.cs213project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens clinic.cs213project3 to javafx.fxml;
    exports clinic.cs213project3;
    exports clinic.cs213project3.view;
    opens clinic.cs213project3.view to javafx.fxml;
}