package com.example.nodesapp;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

// Class representing a connection between sockets
public class Connection extends CubicCurve {
    private Socket inputSocket;     // the input socket connected to the connection
    private Socket outputSocket;    // the output socket connected to the connection
    private boolean isStartPointInput;  // is the input-socket the starting point of the line or not

    // socket: socket to connect this connection to
    public Connection(Socket socket) {
        // setting fields according to socket being input/output
        if(socket.isInput()) {
            this.inputSocket = socket;
            this.isStartPointInput = true;
        } else {
            this.outputSocket = socket;
            this.isStartPointInput = false;
        }
        // binding the start point of the line to the sockets position
        this.bindStartPointToSocket(socket);
        // setting up listeners that takes this connection to the front of canvas on change of position
        this.setPositionListeners();
        // initializing apperance of the line
        initAppearance();
        // disabling clickable (to not mess up clicking on sockets)
        this.setDisable(true);
        // setting mouse event
        setMouseEvents();

        bindControlPoints();
    }

    // binding the control points of the Bazier curve, to be affected by the start/end points
    // this is for visual aesthetics
    private void bindControlPoints() {
        this.controlX1Property().bind(Bindings.createDoubleBinding(() ->
                        this.startXProperty().get() + (this.endXProperty().get() - this.startXProperty().get()) / 3,
                this.startXProperty(), this.endXProperty()
        ));
        this.controlX2Property().bind(Bindings.createDoubleBinding(() ->
                        this.endXProperty().get() + (this.startXProperty().get() - this.endXProperty().get()) / 3,
                this.startXProperty(), this.endXProperty()
        ));

        this.controlY1Property().bind(this.startYProperty());
        this.controlY2Property().bind(this.endYProperty());
    }

    // setting up position listeners that brings connection to front of canvas on change of position
    private void setPositionListeners() {
        ChangeListener<Number> listener = (obs, oldVal, newVal) ->
                this.toFront();

        startXProperty().addListener(listener);
        startYProperty().addListener(listener);
        endXProperty().addListener(listener);
        endYProperty().addListener(listener);
    }

    // detaching the socket connected to the start point of the curve
    private void detachStartPointSocket() {
        if(!this.isStartPointInput && this.outputSocket != null) {
            this.outputSocket.removeConnection(this);
            this.outputSocket = null;
        } else if (this.inputSocket != null){
            this.inputSocket.removeConnection(this);
            this.inputSocket = null;
        }
    }

    // detaching the socket connected to the end point of the curve
    private void detachEndPointSocket() {
        if(this.isStartPointInput && this.outputSocket != null) {
            this.outputSocket.removeConnection(this);
            this.outputSocket = null;
        } else if (this.inputSocket != null) {
            this.inputSocket.removeConnection(this);
            this.inputSocket = null;
        }
    }

    // setting mouse events
    private void setMouseEvents() {
        // handling hovering styling
        this.setOnMouseEntered(event -> {
            this.setStroke(Color.WHITE);
            this.setStrokeWidth(5);
        });

        // handling hovering styling
        this.setOnMouseExited(event -> {
            initAppearance();
        });
        // handling detaching of connection from socket on mouse press
        this.setOnMousePressed(event -> {
            // finding out if mouse pressed closer to startPoint/endPoint
            Point2D startPoint = new Point2D(this.getStartX(), this.getStartY());
            Point2D endPoint = new Point2D(this.getEndX(), this.getEndY());
            Point2D mousePoint = new Point2D(event.getSceneX(), event.getSceneY());
            double startDist = mousePoint.distance(startPoint);
            double endDist = mousePoint.distance(endPoint);
            // if closer to start point
            if(startDist < endDist) {
                // detach start point and bind it to mouse
                this.detachStartPointSocket();
                this.bindStartPoint(Director.getInstance().mouseXProperty(), Director.getInstance().mouseYProperty());
            } else {
                // detach end point and bind it to mouse
                this.detachEndPointSocket();
                this.bindEndPoint(Director.getInstance().mouseXProperty(), Director.getInstance().mouseYProperty());
            }
            // set this to be the current connection
            Director.getInstance().getConnectionManager().startConnectionModifyingState(this);
            event.consume();
        });
    }

    // initializing UI style
    private void initAppearance() {
        setStroke(Color.ORANGE);
        setStrokeWidth(2);
        setFill(null);
    }

    // binding the curve's start point position to the given socket's position
    // socket: the socket we want to bind to
    public void bindStartPointToSocket(Socket socket) {
        startXProperty().bind(socket.getSocketXproperty());
        startYProperty().bind(socket.getSocketYproperty());
    }

    // binding the curve's end point position to the given socket's position
    // socket: the socket we want to bind to
    public void bindEndPointToSocket(Socket socket) {
        endXProperty().bind(socket.getSocketXproperty());
        endYProperty().bind(socket.getSocketYproperty());
    }

    // binding the position of thepoint (start/end of curve) that is loose (not connected to a socket)
    // to the position of the given socket
    // socket: the socket we want to bind to
    public void bindLoosePointToSocket(Socket socket) {
        if((this.isStartPointInput && this.inputSocket == null) || (!this.isStartPointInput && this.inputSocket != null) ) {
            this.bindStartPointToSocket(socket);
        } else {
            this.bindEndPointToSocket(socket);
        }
    }

    // binding the end point's position to the given properties
    // xProp: property we want to bind the end points x position to
    // yProp: property we want to bind the end points y position to
    public void bindEndPoint(DoubleProperty xProp, DoubleProperty yProp) {
        endXProperty().bind(xProp);
        endYProperty().bind(yProp);
    }

    // binding the start point's position to the given properties
    // xProp: property we want to bind the start points x position to
    // yProp: property we want to bind the start points y position to
    public void bindStartPoint(DoubleProperty xProp, DoubleProperty yProp) {
        startXProperty().bind(xProp);
        startYProperty().bind(yProp);
    }

    // when our connection is connected to a socket on one side,
    // and the second side is still loose, this checks if the existing socket
    // can connect to the given socket, with all sorts of checks described inside the function.
    // socket: the socket that we want to check if its valid to connect this connection to
    public boolean checkValidSecondSocket(Socket socket) {
        // getting socket on first side of connection
        Socket existingSocket = this.inputSocket != null ? this.inputSocket : this.outputSocket;

        // make sure we connect input<->output and not output<->output or input<->input
        boolean input2output = existingSocket.isInput() != socket.isInput();
        // make sure we're not connecting output socket to input socket of same function node
        // (in future maybe check that the func node graph is DAG)
        boolean differentFuncNodes = existingSocket.getParent() != socket.getParent();
        // making sure a connection with same input/output does not already exist
        boolean connectionIsUnique = true;
        for (Connection conn : Director.getInstance().getConnectionManager().connections) {

            if ((conn.getInputSocket() == this.getExistingSocket() && conn.getOutputSocket() == socket) ||
                    (conn.getInputSocket() == socket && conn.getOutputSocket() == this.getExistingSocket())) {
                connectionIsUnique = false;
                break;
            }
        }

        // returning AND of all checks
        return input2output && differentFuncNodes && connectionIsUnique;
    }

    // ###########################
    // ### getters and setters ###
    // ###########################

    public Socket getInputSocket() {
        return inputSocket;
    }

    public void setInputSocket(Socket inputSocket) {
        this.inputSocket = inputSocket;
    }

    public Socket getOutputSocket() {
        return outputSocket;
    }

    public void setOutputSocket(Socket outputSocket) {
        this.outputSocket = outputSocket;
    }

    public void setEndPoint(double x, double y) {
        setEndX(x);
        setEndY(y);
        this.toFront();
    }

    public Socket getExistingSocket() {
        if (this.inputSocket != null) {
            return this.inputSocket;
        }
        return this.outputSocket;
    }

    public void setMissingSocket(Socket socket) {
        if (this.inputSocket == null) {
            this.inputSocket = socket;
        } else {
            this.outputSocket = socket;
        }
    }
}
