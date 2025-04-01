package com.example.nodesapp;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

// Class representing input/output socket on a node
public class Socket extends BorderPane {
    private Circle circle;      // drawing circle for UI of socket
    private String name;        // name of the socket (this will actually be the title)
    private boolean isInput;    // is this socket input or output
    private SocketValue value;
    private List<Connection> connections = new ArrayList<>();   // connections that connect to this socket

    private Director director;  // we just keep it here for convenience

    // name: title of this socket
    // initValue: initial value of the socket (the default value)
    // isInput: is this socket an input socket or an output socket
    public Socket(String name, SocketValue initValue, boolean isInput) {
        // init fields
        this.name = name;
        this.value = initValue;
        this.isInput = isInput;
        this.director = Director.getInstance();
        // init drawing
        this.drawSocket();
        // set mouse events
        this.setMouseEvents();
    }

    // finds the socket that is lying underneath the mouse on an event
    // event: the mouse event that we want to extract the mouse position from
    public Socket pickSocket(MouseEvent event) {
        Point2D scenePoint = new Point2D(event.getSceneX(), event.getSceneY());

        // This gets the top-most node (in the scene) at the given coordinates (we hope this will be a func node)
        Node targetNode = this.director.getScene().getRoot().getChildrenUnmodifiable().stream()
                .filter(node -> node.getBoundsInParent().contains(scenePoint))
                .findFirst()
                .orElse(null);
        // if the node we found is a func-node
        if(targetNode instanceof FunctionNode) {
            // this gets the top-most node in the func-node (if we're inside a function node)
            // we hope this will be a socket
            Point2D newScenePoint = new Point2D(scenePoint.getX() - targetNode.getLayoutX(), scenePoint.getY() - targetNode.getLayoutY());
            targetNode = ((FunctionNode) targetNode).getChildrenUnmodifiable().stream()
                    .filter(node -> node.getBoundsInParent().contains(newScenePoint))
                    .findFirst()
                    .orElse(null);
            // if the node we found is a socket
            if(targetNode instanceof Socket) {
                // return this socket
                return (Socket) targetNode;
            }
        }
        // if socket not found, return null
        return null;
    }

    // set mouse event for this sockets
    private void setMouseEvents() {

        // if this socket is pressed, try to enter connection-creation state
        circle.setOnMousePressed(event -> {
            this.director.getConnectionManager().startConnectionModifyingStateFromSocket(this);
            event.consume();
        });
        // if the mouse is released after dragging from this socket to some other point
        circle.setOnMouseReleased(event -> {
            // point of mouse event
            Point2D scenePoint = new Point2D(event.getSceneX(), event.getSceneY());
            // find socket below mouse (returns null if no socket found)
            Socket targetSocket = pickSocket(event);
            // if it's not null (a socket was found)
            if(targetSocket != null) {
                // end connection-creation state, and connect currentConnection to the socket we found
                this.director.getConnectionManager().endConnectionModifyingState(targetSocket);
            }
            event.consume();
        });
        // if we drag the mouse, just dont forger to update mouse properties for animation update
        circle.setOnMouseDragged(event -> {
            this.director.updateMouseProperties(event);
            event.consume();
        });
    }

    // handles UI-styling for this socket
    private void drawSocket() {
        // Create the visual socket
        circle = new Circle(6);
        circle.setFill(isInput ? Color.LIGHTBLUE : Color.LIGHTGREEN);
        circle.setStroke(Color.WHITE);

        Label label = new Label(name);
        label.setStyle("-fx-text-fill: white;");

        // Layout based on input/output
        if (isInput) {
            setLeft(circle);
            setRight(label);
        } else {
            setLeft(label);
            setRight(circle);
        }

        setPadding(new Insets(5));
    }

    // add a connection to this socket (meaning a connection that connects to this socket)
    // conn: the connection to add to this socket
    public void addConnection(Connection conn) {
        if (!this.connections.contains(conn)) this.connections.add(conn);
    }

    // remove a connection from this socket
    // conn: the connection to remove from this socket
    public void removeConnection(Connection conn) {
        this.connections.remove(conn);
    }

    // ###########################
    // ### getters and setters ###
    // ###########################

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    public double getSocketX() {
        return getParent().getLayoutX() + getLayoutX() + circle.getLayoutX();
    }

    public double getSocketY() {
        return getParent().getLayoutY() + getLayoutY() + circle.getLayoutY();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public NumberBinding getSocketXproperty() {
        NumberBinding sumX = Bindings.add(this.getParent().layoutXProperty(), this.layoutXProperty());
        sumX = Bindings.add(sumX, this.getCircle().layoutXProperty());
        return sumX;
    }

    public NumberBinding getSocketYproperty() {
        NumberBinding sumY = Bindings.add(this.getParent().layoutYProperty(), this.layoutYProperty());
        sumY = Bindings.add(sumY, this.getCircle().layoutYProperty());
        return sumY;
    }

    public SocketValue getValue() {
        return value;
    }

    public void setValue(SocketValue value) {
        this.value = value;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
