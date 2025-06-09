package com.example.nodesapp;

import com.example.nodesapp.FN.*;
import com.example.nodesapp.FN.Boolean.*;
import com.example.nodesapp.FN.Converters.ConverterBooleanToNumericalFN;
import com.example.nodesapp.FN.Converters.ConverterNumericalToBooleanFN;
import com.example.nodesapp.FN.Image.ImageBrightnessFN;
import com.example.nodesapp.FN.Image.ImageConstantFN;
import com.example.nodesapp.FN.Image.ImageDisplayFN;
import com.example.nodesapp.FN.Numerical.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class AddNodesMenu extends ContextMenu {
    public AddNodesMenu() {

        Menu numericalMenu = new Menu("Numerical");
        Menu booleanMenu = new Menu("Boolean");
        Menu convertersMenu = new Menu("Converters");
        Menu imageMenu = new Menu("Image");

        // numerical
        MenuItem numericalAdderItem = new MenuItem("Adder");
        MenuItem numericalConstantItem = new MenuItem("Constant");
        MenuItem numericalDisplayItem = new MenuItem("Display");
        MenuItem numericalSubtractorItem = new MenuItem("Subtractor");
        MenuItem numericalMultiplierItem = new MenuItem("Multiplier");
        MenuItem numericalDividerItem = new MenuItem("Divider");
        // boolean
        MenuItem booleanConstantItem = new MenuItem("Constant");
        MenuItem booleanDisplayItem = new MenuItem("Display");
        MenuItem booleanNotItem = new MenuItem("Not");
        MenuItem booleanAndItem = new MenuItem("And");
        MenuItem booleanOrItem = new MenuItem("Or");
        MenuItem booleanXorItem = new MenuItem("Xor");
        MenuItem booleanNandItem = new MenuItem("Nand");
        // image
        MenuItem imageConstantItem = new MenuItem("Constant");
        MenuItem imageDisplayItem = new MenuItem("Display");
        MenuItem imageBrightnessItem = new MenuItem("Brightness");
        // converters
        MenuItem converterNumericalToBooleanItem = new MenuItem("Numerical to Boolean");
        MenuItem converterBooleanToNumericalItem = new MenuItem("Boolean to Numerical");

        // numerical
        numericalAdderItem.setOnAction(e -> addNodeAndSetPosition(new NumericalAdderFN(0, 0)));
        numericalConstantItem.setOnAction(e -> addNodeAndSetPosition(new NumericalConstantFN(0, 0)));
        numericalDisplayItem.setOnAction(e -> addNodeAndSetPosition(new NumericalDisplayFN(0, 0)));
        numericalSubtractorItem.setOnAction(e -> addNodeAndSetPosition(new NumericalSubtractorFN(0, 0)));
        numericalMultiplierItem.setOnAction(e -> addNodeAndSetPosition(new NumericalMultiplierFN(0, 0)));
        numericalDividerItem.setOnAction(e -> addNodeAndSetPosition(new NumericalDividerFN(0, 0)));
        // boolean
        booleanConstantItem.setOnAction(e -> addNodeAndSetPosition(new BooleanConstantFN(0, 0)));
        booleanDisplayItem.setOnAction(e -> addNodeAndSetPosition(new BooleanDisplayFN(0, 0)));
        booleanNotItem.setOnAction(e -> addNodeAndSetPosition(new BooleanNotFN(0, 0)));
        booleanAndItem.setOnAction(e -> addNodeAndSetPosition(new BooleanAndFN(0, 0)));
        booleanOrItem.setOnAction(e -> addNodeAndSetPosition(new BooleanOrFN(0, 0)));
        booleanXorItem.setOnAction(e -> addNodeAndSetPosition(new BooleanXorFN(0, 0)));
        booleanNandItem.setOnAction(e -> addNodeAndSetPosition(new BooleanNandFN(0, 0)));
        // image
        imageConstantItem.setOnAction(e -> addNodeAndSetPosition(new ImageConstantFN(0, 0)));
        imageDisplayItem.setOnAction(e -> addNodeAndSetPosition(new ImageDisplayFN(0, 0)));
        imageBrightnessItem.setOnAction(e -> addNodeAndSetPosition(new ImageBrightnessFN(0, 0)));
        // converters
        converterNumericalToBooleanItem.setOnAction(e -> addNodeAndSetPosition(new ConverterNumericalToBooleanFN(0, 0)));
        converterBooleanToNumericalItem.setOnAction(e -> addNodeAndSetPosition(new ConverterBooleanToNumericalFN(0, 0)));


        // adding items to menus
        numericalMenu.getItems().addAll(numericalAdderItem, numericalConstantItem, numericalDisplayItem, numericalSubtractorItem, numericalMultiplierItem, numericalDividerItem);
        booleanMenu.getItems().addAll(booleanConstantItem, booleanDisplayItem, booleanNotItem, booleanAndItem, booleanOrItem, booleanXorItem, booleanNandItem);
        convertersMenu.getItems().addAll(converterNumericalToBooleanItem, converterBooleanToNumericalItem);
        imageMenu.getItems().addAll(imageConstantItem, imageDisplayItem, imageBrightnessItem);

        this.getItems().addAll(numericalMenu, booleanMenu, convertersMenu, imageMenu);
    }

    private void addNodeAndSetPosition(FunctionNode node) {
        Director director = Director.getInstance();

        node.setLayoutX(director.getMouseX());
        node.setLayoutY(director.getMouseY());

        director.getNodesManager().addNode(node);
    }
}
