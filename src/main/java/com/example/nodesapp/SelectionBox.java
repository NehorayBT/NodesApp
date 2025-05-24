package com.example.nodesapp;

import com.example.nodesapp.FN.FunctionNode;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectionBox extends Rectangle {
    DoubleProperty sourceX;
    DoubleProperty sourceY;

    ChangeListener<Number> boxChangeListener;

    public SelectionBox(double x, double y) {
        this.sourceX = new SimpleDoubleProperty();
        this.sourceY = new SimpleDoubleProperty();

        this.boxChangeListener = (obs, oldVal, newVal) -> onBoxChange();

        this.sourceX.set(x);
        this.sourceY.set(y);

        this.setFill(Color.web("#00000040")); // gray with 50% opacity
        this.setStroke(Color.web("#ffffff")); // solid black border
        this.setStrokeWidth(1);

        this.bindToMouse();
    }

    private void bindToMouse() {
        Director director = Director.getInstance();
        DoubleProperty mouseX = director.mouseXProperty();
        DoubleProperty mouseY = director.mouseYProperty();

        // #####################################
        // ## binding width & height to mouse ##
        // #####################################

        BooleanBinding isWidthPositive = Bindings.createBooleanBinding(
                () -> mouseX.get() - this.sourceX.get() >= 0,
                mouseX, this.sourceX
        );

        BooleanBinding isHeightPositive = Bindings.createBooleanBinding(
                () -> mouseY.get() - this.sourceY.get() >= 0,
                mouseY, this.sourceY
        );

        DoubleProperty posWidth = new SimpleDoubleProperty();
        DoubleProperty posHeight = new SimpleDoubleProperty();
        DoubleProperty negWidth = new SimpleDoubleProperty();
        DoubleProperty negHeight = new SimpleDoubleProperty();

        posWidth.bind(mouseX.subtract(this.sourceX));
        posHeight.bind(mouseY.subtract(this.sourceY));
        negWidth.bind(posWidth.multiply(-1));
        negHeight.bind(posHeight.multiply(-1));

        this.widthProperty().bind(
                Bindings.when(isWidthPositive)
                .then(posWidth)
                .otherwise(negWidth)
        );

        this.heightProperty().bind(
                Bindings.when(isHeightPositive)
                        .then(posHeight)
                        .otherwise(negHeight)
        );

        // ############################
        // ## binding X & Y to mouse ##
        // ############################

        this.xProperty().bind(sourceX);
        this.yProperty().bind(sourceY);

        isWidthPositive.addListener((obs, oldVal, newVal) -> {
            if(newVal) {
                this.xProperty().bind(sourceX);
            } else {
                this.xProperty().bind(mouseX);
            }
        });

        isHeightPositive.addListener((obs, oldVal, newVal) -> {
            if(newVal) {
                this.yProperty().bind(sourceY);
            } else {
                this.yProperty().bind(mouseY);
            }
        });
    }

    public void onBoxChange() {
        ObservableList<FunctionNode> nodes = Director.getInstance().getNodesManager().getNodes();

        for(FunctionNode node : nodes) {
            if(node.isNodeWithinRect(this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
                Director.getInstance().getSelectionManager().selectNodeIfNotPreviouslySelected(node);
            } else {
                Director.getInstance().getSelectionManager().deselectNodeIfNotPreviouslySelected(node);
            }
        }
    }

    private void activateBoxChangeListener() {
        this.widthProperty().addListener(boxChangeListener);
        this.heightProperty().addListener(boxChangeListener);
    }

    private void deactivateBoxChangeListener() {
        this.widthProperty().removeListener(boxChangeListener);
        this.heightProperty().removeListener(boxChangeListener);
    }

    public void activate(MouseEvent event) {
        Director.getInstance().getCanvas().getChildren().add(this);

        if(!event.isControlDown()) {
            Director.getInstance().getSelectionManager().deselectAll();
        }

        this.activateBoxChangeListener();
        this.sourceX.set(event.getSceneX());
        this.sourceY.set(event.getSceneY());
    }

    public void deactivate() {
        this.deactivateBoxChangeListener();
        Director.getInstance().getCanvas().getChildren().remove(this);
    }
}
