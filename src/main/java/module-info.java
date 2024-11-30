module com.example.stopperora {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.stopperora to javafx.fxml;
    exports com.example.stopperora;
}