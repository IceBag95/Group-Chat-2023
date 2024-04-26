module com.example.diet2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.diet2 to javafx.fxml;
    exports com.example.diet2;
}