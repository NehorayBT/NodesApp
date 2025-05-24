module com.example.nodesapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.nodesapp to javafx.fxml;
    exports com.example.nodesapp;
    exports com.example.nodesapp.FN;
    opens com.example.nodesapp.FN to javafx.fxml;
}