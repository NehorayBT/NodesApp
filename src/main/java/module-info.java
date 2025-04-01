module com.example.nodesapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nodesapp to javafx.fxml;
    exports com.example.nodesapp;
}