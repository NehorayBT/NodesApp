package com.example.nodesapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

// Class representing a function node
public abstract class FunctionNode extends VBox {
    protected List<Socket> inputSockets = new ArrayList<>();  // all of this node's input sockets
    protected List<Socket> outputSockets = new ArrayList<>(); // all of this node's output sockets
    protected double mouseX, mouseY;
    protected String name = "Function node";

    // x, y: the location in the pane
    public FunctionNode(double x, double y) {
        // drawing the UI of the func-node
        drawNode();

        // Set position
        setLayoutX(x);
        setLayoutY(y);

        // initialize sockets
        initSockets();

        // Make the node draggable
        makeDraggable();
    }

    // setting events to make the node draggable
    protected void makeDraggable() {
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
    }

    // drawing the UI of the node (changes between different types of nodes)
    protected void drawNode() {
        // Node styling
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #444444; -fx-border-color: #666666; -fx-border-radius: 5;");

        // Node header
        Label title = new Label(name);
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        getChildren().add(title);
    }

    // bringing node to front on mouse press
    protected void onMousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
        toFront(); // Bring to front when selected
        Director.getInstance().getConnectionManager().bringAllToFront();
    }

    // moving node on mouse drag
    protected void onMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - mouseX;
        double deltaY = event.getSceneY() - mouseY;
        setLayoutX(getLayoutX() + deltaX);
        setLayoutY(getLayoutY() + deltaY);
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    // adding input socket
    protected void addInputSocket(Socket socket) {
        inputSockets.add(socket);
        getChildren().add(socket);
    }

    // adding output socket
    protected void addOutputSocket(Socket socket) {
        outputSockets.add(socket);
        getChildren().add(socket);
    }

    // a function that adds all required nodes to the node (this is node-specific)
    protected abstract void initSockets();

    // a function that takes all values in the input-sockets, and calculates values for the output-sockets
    protected abstract void updateSocketValues();
}
