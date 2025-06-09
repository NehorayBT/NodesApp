module com.example.nodesapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.nodesapp to javafx.fxml;
    exports com.example.nodesapp;
    exports com.example.nodesapp.FN;
    opens com.example.nodesapp.FN to javafx.fxml;
    exports com.example.nodesapp.FN.Numerical;
    opens com.example.nodesapp.FN.Numerical to javafx.fxml;
    exports com.example.nodesapp.FN.Boolean;
    opens com.example.nodesapp.FN.Boolean to javafx.fxml;
}