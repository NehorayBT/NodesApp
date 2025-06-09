package com.example.nodesapp.FN;

import com.example.nodesapp.Director;
import com.example.nodesapp.SelectionManager;
import com.example.nodesapp.Socket;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


// Class representing a function node
public abstract class FunctionNode extends VBox { // all of this node's output sockets
    protected double mouseX, mouseY;
    protected StringProperty name = new SimpleStringProperty("Function node");
    private Rectangle strokeRect;

    // x, y: the location in the pane
    public FunctionNode(double x, double y) {
        // drawing the UI of the func-node
        drawNode();

        // Set position
        setLayoutX(x);
        setLayoutY(y);

        // initialize sockets
        initSockets();

        initElements();

        // Make the node draggable
        bindMouseEvents();
    }

    // setting events to make the node draggable
    protected void bindMouseEvents() {
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
    }

    // drawing the UI of the node (changes between different types of nodes)
    protected void drawNode() {
        // Node styling
        this.setMinWidth(150);
        this.setMaxWidth(300);
        this.setStyle("-fx-background-color: #2D2D2D; -fx-background-radius: 5;");

        // Title bar with color
        HBox titleBar = new HBox();
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setBackground(new Background(new BackgroundFill(
                Color.web("#4A90E2"), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)));

        // label for the title bar
        Label titleLabel = new Label();
        titleLabel.textProperty().bind(this.name);
        titleLabel.setTextFill(Color.WHITE);
        titleBar.getChildren().add(titleLabel);

        // adding titleBar to node
        getChildren().add(titleBar);

        // creating a rectangle that will be the stroke of this node (if needed for later)
        createStrokeRect();
    }

    // a rectangle that's behind this node, used for stroke effect
    protected void createStrokeRect() {
        int strokeWeight = 2;
        this.strokeRect = new Rectangle();
        this.strokeRect.setFill(Color.WHITE);
        this.strokeRect.layoutXProperty().bind(this.layoutXProperty().subtract(strokeWeight));
        this.strokeRect.layoutYProperty().bind(this.layoutYProperty().subtract(strokeWeight));
        this.strokeRect.widthProperty().bind(this.widthProperty().add(strokeWeight * 2));
        this.strokeRect.heightProperty().bind(this.heightProperty().add(strokeWeight * 2));
        this.strokeRect.setArcWidth(10);
        this.strokeRect.setArcHeight(10);
        //this.strokeRect.setViewOrder(5);
    }

    // bringing node to front on mouse press
    protected void onMousePressed(MouseEvent event) {
        SelectionManager selectionManager = Director.getInstance().getSelectionManager();
        if(!selectionManager.isNodeSelected(this)) {
            if(!event.isControlDown()) {
                selectionManager.deselectAll();
            }
            selectionManager.selectNode(this);
            selectionManager.updatePreviouslySelectedNodes();
        } else {
            if(event.isControlDown()) {
                selectionManager.deselectNode(this);
                selectionManager.updatePreviouslySelectedNodes();
            }
        }
        selectionManager.onMousePressed(event);
        event.consume();
    }

    // moving node on mouse drag
    protected void onMouseDragged(MouseEvent event) {
        Director.getInstance().getSelectionManager().onMouseDragged(event);
        event.consume();
    }

    // adding a socket to this node's UI
    protected void addSocket(Socket<?> socket) {
        if(socket.isInput()) {
            socket.setPadding(new Insets(10, 0, 10, -5));  // Negative left padding
            socket.setAlignment(Pos.CENTER_LEFT);
        } else {
            socket.setPadding(new Insets(10, -5, 10, 0));  // Negative right padding
            socket.setAlignment(Pos.CENTER_RIGHT);
        }
        getChildren().add(socket);
    }

    // adding multiple sockets to this node's UI
    protected void addAllSockets(Socket<?>... sockets) {
        for (Socket<?> socket : sockets) {
            this.addSocket(socket);
        }
    }

    protected void addElement(Node element) {
        this.getChildren().add(element);
    }

    // a function that adds all required elements (that are not sockets) to the node
    protected void initElements() {}

    // a function that adds all required nodes to the node (this is node-specific)
    protected abstract void initSockets();

    // a function that takes all values in the input-sockets, and calculates values for the output-sockets
    public abstract void updateSocketValues();

    public boolean isPointWithingNode(double x, double y) {
        boolean xWithin = x >= this.getLayoutX() & x <= this.getLayoutX() + this.getWidth();
        boolean yWithin = y >= this.getLayoutY() & y <= this.getLayoutY() + this.getHeight();

        return xWithin & yWithin;
    }

    // given that x, y are top-left corner
    public boolean isNodeWithinRect(double x, double y, double width, double height) {
        boolean nodeLeftOfRect = this.getLayoutX() + this.getWidth() < x;
        boolean nodeRightOfRect = this.getLayoutX() > x + width;
        boolean nodeAboveRect = this.getLayoutY() + this.getHeight() < y;
        boolean nodeBelowRect = this.getLayoutY() > y + height;

        return !(nodeLeftOfRect || nodeRightOfRect || nodeAboveRect || nodeBelowRect);
    }

    public void showStroke() {
        if(!Director.getInstance().getCanvas().getChildren().contains(this.strokeRect)) {
            Director.getInstance().getCanvas().getChildren().add(this.strokeRect);
            this.toFront();
        }
    }

    public void hideStroke() {
        Director.getInstance().getCanvas().getChildren().remove(this.strokeRect);
    }

    public void toFront() {
        this.strokeRect.toFront();
        super.toFront();
    }

    public abstract void terminateSockets();
}
