package com.example.nodesapp;

import com.example.nodesapp.FN.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class AddNodesMenu extends ContextMenu {
    public AddNodesMenu() {

        Menu numericalMenu = new Menu("Numerical");
        Menu booleanMenu = new Menu("Boolean");

        // numerical
        MenuItem numericalAdderItem = new MenuItem("Adder");
        MenuItem numericalConstantItem = new MenuItem("Constant");
        MenuItem numericalDisplayItem = new MenuItem("Display");
        // boolean
        MenuItem booleanConstantItem = new MenuItem("Constant");

        // numerical
        numericalAdderItem.setOnAction(e -> addNodeAndSetPosition(new AdderFN(0, 0)));
        numericalConstantItem.setOnAction(e -> addNodeAndSetPosition(new NumberConstantFN(0, 0)));
        numericalDisplayItem.setOnAction(e -> addNodeAndSetPosition(new NumberDisplayFN(0, 0)));
        // boolean
        booleanConstantItem.setOnAction(e -> addNodeAndSetPosition(new BooleanConstantFN(0, 0)));

        numericalMenu.getItems().addAll(numericalAdderItem, numericalConstantItem, numericalDisplayItem);
        booleanMenu.getItems().addAll(booleanConstantItem);

        this.getItems().addAll(numericalMenu, booleanMenu);
    }

    private void addNodeAndSetPosition(FunctionNode node) {
        Director director = Director.getInstance();

        node.setLayoutX(director.getMouseX());
        node.setLayoutY(director.getMouseY());

        director.getNodesManager().addNode(node);
    }
}
