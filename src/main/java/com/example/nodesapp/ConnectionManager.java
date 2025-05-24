package com.example.nodesapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionManager {
    ObservableList<Connection<?>> connections;   // a list of all the connections
    Connection<?> currentConnection;   // to store connection that is currently being made

    public ConnectionManager() {
        this.connections = FXCollections.observableArrayList();
        this.currentConnection = null;

        // bind connections to the canvas children
        bindConnectionsToCanvasChildren();

    }

    // a function that binds the connections list to the canvas children list,
    // so that every connection that is added here will be automatically added to the canvas children.
    private void bindConnectionsToCanvasChildren() {
        ObservableList<Node> canvasChildren = Director.getInstance().getCanvas().getChildren();
        // Add a listener to list1
        this.connections.addListener((ListChangeListener<Connection<?>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    canvasChildren.addAll(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    canvasChildren.removeAll(change.getRemoved());
                }
            }
        });
    }

    // creating a new connection and connect it to the socket,
    // getting into connection-modifying state
    // socket: the socket to attach the connection to.
    public void startConnectionModifyingStateFromSocket(Socket<?> socket) {
        // if no connection is currently already being made
        if(this.currentConnection == null) {
            // start a new connection and bind its end to the mouse
            Connection<?> conn = socket.createConnectionFromThis();
            conn.bindEndPoint(Director.getInstance().mouseXProperty(), Director.getInstance().mouseYProperty());
            // add it to the canvas
            this.connections.add(conn);
            // start the connection-modifying state
            this.startConnectionModifyingState(conn);
        }
    }

    // when taking an established connection and detaching it from a socket,
    // we call this function to handle connection-modifying state
    // conn: connection that needs to be detached
    public  void startConnectionModifyingState(Connection<?> conn) {
        // updating current connection to the one we got
        this.setCurrentConnection(conn);
        // disabling clickes, because they mess up clicking on the sockets,
        // and we need to click sockets in order to end connection-modifying process
        disableClickables();
    }

    // for ending the connection-modifying state
    // socket: the socket to attach other side of current connection to
    public void endConnectionModifyingState(Socket<?> socket) {
        // if were on connection-creation state, and the socket we want to connect the other side of
        // the current connection into is a valid socket
        if (this.currentConnection != null && this.currentConnection.checkValidSecondSocket(socket) && this.currentConnection.matchingSocketType(socket)) {
            // we're binding the connection other end's position to the socket
            this.currentConnection.bindLoosePointToSocket(socket);
            // Complete the connection to this socket
            this.currentConnection.setMissingSocketIfTypeMatches(socket);
            // adding connection
            this.addConnection(this.currentConnection);
            // exiting connection-modifying state
            this.setCurrentConnection(null);
            // enabling connection clicks
            this.enableClickables();
        }
    }

    // disabling the ability to click on connections
    private void disableClickables() {
        for (Connection<?> otherConn : this.connections) {
            otherConn.setDisable(true);
        }
    }

    // enabling the ability to click on connections
    private void enableClickables() {
        for (Connection<?> otherConn : this.connections) {
            otherConn.setDisable(false);
        }
    }

    // adding a connection (not temporary, rather established one) to the connections list.
    // also updates the sockets of the connection on the new connection that is being made
    // conn: the connection to add
    public void addConnection(Connection<?> conn) {
        if(!this.connections.contains(conn)) {
            this.connections.add(conn);
        }
    }

    // removing a connection from the connection list.
    // also exits connection-editing mode if inside,
    // and updating the sockets and canvas on the removal
    // conn: the connection to remove
    public void removeConnection(Connection<?> conn) {
        // removing from connection list
        this.connections.remove(conn);
        // exiting connection-creation state
        if(this.currentConnection == conn) {
            this.currentConnection = null;
            this.enableClickables();
        }
        // updating sockets
        if(conn.getInputSocket() != null) {
            conn.getInputSocket().removeConnectionIfTypeMatches(conn);
        }
        if(conn.getOutputSocket() != null) {
            conn.getOutputSocket().removeConnectionIfTypeMatches(conn);
        }
    }

    // ###########################
    // ### getters and setters ###
    // ###########################

    public Connection<?> getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(Connection<?> currentConnection) {
        this.currentConnection = currentConnection;
    }
}
