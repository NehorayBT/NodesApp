package com.example.nodesapp;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

// this class acts as the main instantenation of the scene containing the nodes
public class Director {

    private static Director instance;

    private Stage stage;    // the main stage
    private Scene scene;    // the scene containing the pane
    private Pane canvas;    // the pane - where all the function-nodes and connections are present

    private NodesManager nodesManager;  // in charge of managing function nodes
    private ConnectionManager connectionManager;    // in charge of managing connections

    private final DoubleProperty mouseX;    // property that contains mouseX
    private final DoubleProperty mouseY;    // property that contains mouseY

    public Director(Stage primaryStage) {
        Director.instance = this;   // for the getInstance() function

        this.stage = primaryStage;

        // initializing fields
        this.canvas = new Pane();
        this.canvas.setStyle("-fx-background-color: #333333;");

        ObservableList<Node> canvasChildren = FXCollections.observableArrayList();
        Bindings.bindContent(canvas.getChildren(), canvasChildren);

        this.scene = new Scene(canvas, 1200, 800);

        this.nodesManager = new NodesManager();
        this.connectionManager = new ConnectionManager();

        this.mouseX = new SimpleDoubleProperty();
        this.mouseY = new SimpleDoubleProperty();

        this.stage.setTitle("Nodes App");
        this.stage.setScene(this.scene);
        this.stage.show();

        // initializing mouse events
        this.setCanvasMouseEvents();

        // Create some example nodes
        AdderFN node1 = new AdderFN(500, 300);
        AdderFN node2 = new AdderFN(800, 300);
        IntegerConstantFN node3 = new IntegerConstantFN(200, 300, 5);
        IntegerConstantFN node4 = new IntegerConstantFN(200, 500, 3);
        this.nodesManager.addAllNodes(node1, node2, node3, node4);

        AdderFN node5 = new AdderFN(800, 500);
        canvasChildren.add(node5);
        canvasChildren.remove(node5);
        canvasChildren.remove(node3);
    }

    // updates the mouse position properties whenever the mouse moves
    // event: the event we want to extract the mouse position from
    public void updateMouseProperties(MouseEvent event) {
        this.mouseX.set(event.getSceneX());
        this.mouseY.set(event.getSceneY());
    }

    // setting up mouse events
    private void setCanvasMouseEvents() {
        // update mouse position properties when it moves
        this.canvas.setOnMouseMoved(this::updateMouseProperties);

        // if you right click while making a connection, delete this connection
        this.canvas.setOnMouseClicked(event -> {
            Connection conn = this.connectionManager.getCurrentConnection();
            if(event.getButton() == MouseButton.SECONDARY && conn != null) {
                this.connectionManager.removeConnection(conn);
            }
            event.consume();
        });
    }

    // ###########################
    // ### getters and setters ###
    // ###########################

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public void setCanvas(Pane canvas) {
        this.canvas = canvas;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public NodesManager getNodesManager() {
        return nodesManager;
    }

    public void setNodesManager(NodesManager nodesManager) {
        this.nodesManager = nodesManager;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public double getMouseX() {
        return mouseX.get();
    }

    public DoubleProperty mouseXProperty() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX.set(mouseX);
    }

    public double getMouseY() {
        return mouseY.get();
    }

    public DoubleProperty mouseYProperty() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY.set(mouseY);
    }

    public static Director getInstance() {
        return Director.instance;
    }
}
