package com.example.nodesapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    List<Connection> connections;   // a list of all the connections
    Connection currentConnection;   // to store connection that is currently being made
    StringProperty state;   // for dev purposes

    public ConnectionManager() {
        this.connections = new ArrayList<>();
        this.currentConnection = null;
        state = new SimpleStringProperty("neutral");

        // for dev purposes
        drawState();
    }

    // for dev purposes
    private void drawState() {
        // Create a Text object
        Text text = new Text(50, 100, "Hello, JavaFX Pane!");
        text.setFont(new Font("Arial", 24));
        text.setFill(Color.WHITE);

        // Add text to the pane
        text.textProperty().bind(this.state);
        Director.getInstance().getCanvas().getChildren().add(text);
    }

    // creating a new connection and connect it to the socket,
    // getting into connection-modifying state
    // socket: the socket to attach the connection to.
    public void startConnectionModifyingStateFromSocket(Socket socket) {
        // if no connection is currently already being made
        if(this.currentConnection == null) {
            // start a new connection and bind its end to the mouse
            Connection conn = new Connection(socket);
            conn.bindEndPoint(Director.getInstance().mouseXProperty(), Director.getInstance().mouseYProperty());
            // add it to the canvas
            Director.getInstance().getCanvas().getChildren().add(conn);
            // start the connection-modifying state
            this.startConnectionModifyingState(conn);
        }
    }

    // when taking an established connection and detaching it from a socket,
    // we call this function to handle connection-modifying state
    // conn: connection that needs to be detached
    public  void startConnectionModifyingState(Connection conn) {
        // for dev purposes
        this.state.set("connection");
        // updating current connection to the one we got
        this.setCurrentConnection(conn);
        // disabling clickes, because they mess up clicking on the sockets,
        // and we need to click sockets in order to end connection-modifying process
        disableClickables();
    }

    // for ending the connection-modifying state
    // socket: the socket to attach other side of current connection to
    public void endConnectionModifyingState(Socket socket) {
        // if were on connection-creation state, and the socket we want to connect the other side of
        // the current connection into is a valid socket
        if (this.currentConnection != null && this.currentConnection.checkValidSecondSocket(socket)) {
            // for dev purposes
            this.state.set("neutral");
            // we're binding the connection other end's position to the socket
            this.currentConnection.bindLoosePointToSocket(socket);
            // Complete the connection to this socket
            this.currentConnection.setMissingSocket(socket);
            // adding connection
            this.addConnection(this.currentConnection);
            // exiting connection-modifying state
            this.setCurrentConnection(null);
            // enabling connection clicks
            this.enableClickables();
        }
    }

    // brings all the connection UI in-front of everything else
    public void bringAllToFront() {
        for (Connection otherConn : this.connections) {
            otherConn.toFront();
        }
    }

    // disabling the ability to click on connections
    private void disableClickables() {
        for (Connection otherConn : this.connections) {
            otherConn.setDisable(true);
        }
    }

    // enabling the ability to click on connections
    private void enableClickables() {
        for (Connection otherConn : this.connections) {
            otherConn.setDisable(false);
        }
    }

    // adding a connection (not temporary, rather established one) to the connections list.
    // also updates the sockets of the connection on the new connection that is being made
    // conn: the connection to add
    public void addConnection(Connection conn) {
        if(!this.connections.contains(conn)) {
            this.connections.add(conn);
        }
        conn.getInputSocket().addConnection(conn);
        conn.getOutputSocket().addConnection(conn);
    }

    // removing a connection from the connection list.
    // also exits connection-editing mode if inside,
    // and updating the sockets and canvas on the removal
    // conn: the connection to remove
    public void removeConnection(Connection conn) {
        // removing from connection list
        this.connections.remove(conn);
        // exiting connection-creation state
        if(this.currentConnection == conn) {
            this.currentConnection = null;
            this.enableClickables();
        }
        // updating sockets
        if(conn.getInputSocket() != null) {
            conn.getInputSocket().removeConnection(conn);
        }
        if(conn.getOutputSocket() != null) {
            conn.getOutputSocket().removeConnection(conn);
        }
        // removing from canvas
        Director.getInstance().getCanvas().getChildren().remove(conn);
    }


    // ###########################
    // ### getters and setters ###
    // ###########################

    public Connection getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(Connection currentConnection) {
        this.currentConnection = currentConnection;
    }
}
