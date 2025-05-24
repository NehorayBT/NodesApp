package com.example.nodesapp;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

// this class acts as the main instantenation of the scene containing the nodes
public class Director {

    private static Director instance;

    private Stage stage;    // the main stage
    private Scene scene;    // the scene containing the pane
    private Pane canvas;    // the pane - where all the function-nodes and connections are present

    private NodesManager nodesManager;  // in charge of managing function nodes
    private ConnectionManager connectionManager;    // in charge of managing connections
    private SelectionManager selectionManager;

    private AddNodesMenu addNodesMenu;

    private final DoubleProperty mouseX;    // property that contains mouseX
    private final DoubleProperty mouseY;    // property that contains mouseY

    boolean ctrlIsPressed;

    public Director(Stage primaryStage) {
        Director.instance = this;   // for the getInstance() function

        this.stage = primaryStage;

        // initializing fields
        this.canvas = new Pane();
        this.canvas.setStyle("-fx-background-color: #333333;");

        this.scene = new Scene(canvas, 1200, 800);
        this.scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        this.mouseX = new SimpleDoubleProperty();
        this.mouseY = new SimpleDoubleProperty();

        this.ctrlIsPressed = false;

        this.nodesManager = new NodesManager();
        this.connectionManager = new ConnectionManager();
        this.selectionManager = new SelectionManager();

        this.addNodesMenu = new AddNodesMenu();

        this.stage.setTitle("Nodes App");
        this.stage.setScene(this.scene);
        this.stage.show();

        // initializing mouse events
        this.setCanvasMouseEvents();

        this.setSceneEventFilters();
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
        this.canvas.setOnMouseDragged(this::updateMouseProperties);

        // if you right click while making a connection, delete this connection
        this.canvas.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                Connection<?> conn = this.connectionManager.getCurrentConnection();
                if(conn != null) {
                    this.connectionManager.removeConnection(conn);
                } else {
                    this.addNodesMenu.hide();
                    this.addNodesMenu.show(this.canvas, event.getScreenX(), event.getScreenY());
                }
            }
            event.consume();
        });

        this.canvas.setOnMousePressed(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                this.addNodesMenu.hide();
                this.selectionManager.getSelectionBox().activate(event);
                event.consume();
            }

        });

        this.canvas.setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                this.selectionManager.getSelectionBox().deactivate();
                this.selectionManager.updatePreviouslySelectedNodes();
            }

        });
    }

    private void setSceneEventFilters() {
        this.scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                this.selectionManager.deleteSelectedNodes();
                event.consume();
            }
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

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public void setSelectionManager(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }
}
