package com.example.nodesapp;

import com.example.nodesapp.FN.FunctionNode;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

// Class representing input/output socket on a node
public class Socket<T> extends HBox {
    private Circle circle;      // drawing circle for UI of socket
    private String name;        // name of the socket (this will actually be the title)
    private boolean isInput;    // is this socket input or output
    private T value;
    private T defaultValue;
    private List<Connection<T>> connections = new ArrayList<>();   // connections that connect to this socket

    private Director director;  // we just keep it here for convenience

    // name: title of this socket
    // initValue: initial value of the socket (the default value)
    // isInput: is this socket an input socket or an output socket
    public Socket(String name, T value, T defaultValue, boolean isInput) {
        // init fields
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.isInput = isInput;
        this.director = Director.getInstance();

        // init drawing
        this.drawSocket();

        // set mouse events
        this.setMouseEvents();
    }

    public Socket(String name, T value, boolean isInput) {
        this(name, value, value, isInput);
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
            // getting scene-node under mouse if it's a socket
            Socket<?> targetSocket = (event.getPickResult().getIntersectedNode() instanceof Circle) ? ((Socket<?>)(((Circle) event.getPickResult().getIntersectedNode()).getParent())) : null;
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
        circle = new Circle(5);
        circle.setFill(isInput ? Color.LIGHTBLUE : Color.LIGHTGREEN);
        circle.setStroke(Color.web("#2D2D2D"));

        // create the label for this socket
        Label label = new Label(name);
        label.setPadding(new Insets(0, 10, 0, 10));
        label.setStyle("-fx-text-fill: white;");

        // Layout based on input/output
        if (isInput) {
            this.getChildren().addAll(circle, label);
            this.setAlignment(Pos.CENTER_LEFT);
        } else {
            this.getChildren().addAll(label, circle);
            this.setAlignment(Pos.CENTER_RIGHT);
        }

        setPadding(new Insets(5));
    }

    // call this function to propagate socket's output to all input-sockets it's connected to
    public void propagateOutput() {
        if(!this.isInput) {
            for (Connection<T> conn : this.connections) {
                this.propagateOutputToConnection(conn);
            }
        } else {
            ((FunctionNode) this.getParent()).updateSocketValues();
        }
    }

    @SuppressWarnings("unchecked")
    // call this function to propagate socket's output to specific connection
    // conn: the connection to propagate output into
    public void propagateOutputToConnection(Connection<T> conn) {
        if(!this.isInput) {
            Socket<T> inputSocket = (Socket<T>) conn.getInputSocket();
            if(inputSocket != null) {
                ((Socket<T>) conn.getInputSocket()).setValue(this.value);
            }
        }
    }

    @SuppressWarnings("all")
    public boolean checkSocketSameType(Socket<?> socket) {
        try {
            socket = (Socket<T>) socket;
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    // add a connection to this socket (meaning a connection that connects to this socket)
    // conn: the connection to add to this socket
    public void addConnection(Connection<T> conn) {
        if (!this.connections.contains(conn)) {
            this.connections.add(conn);
        }
    }

    @SuppressWarnings("all")
    public void addConnectionIfTypeMatches(Connection<?> conn) {
        if(conn.matchingSocketType(this)) {
            this.addConnection((Connection<T>) conn);
        }
    }

    // remove a connection from this socket
    // conn: the connection to remove from this socket
    public void removeConnection(Connection<T> conn) {
        this.connections.remove(conn);
        if(this.isInput) {
            this.setValue(defaultValue);
        }
    }

    @SuppressWarnings("all")
    public void removeConnectionIfTypeMatches(Connection<?> conn) {
        if(conn.matchingSocketType(this)) {
            this.removeConnection((Connection<T>) conn);
        }
    }

    public Connection<T> createConnectionFromThis() {
        return new Connection<T>(this);
    }

    public void terminate() {
        List<Connection<T>> connList = new ArrayList<>(this.connections);
        for(Connection<T> conn : connList) {
            this.director.getConnectionManager().removeConnection(conn);
        }
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.propagateOutput();
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<Connection<T>> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection<T>> connections) {
        this.connections = connections;
    }
}
