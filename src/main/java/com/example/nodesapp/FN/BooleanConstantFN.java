package com.example.nodesapp.FN;

import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class BooleanConstantFN extends FunctionNode{

    private Socket<Boolean> output;

    private ToggleGroup group;
    private RadioButton trueButton;
    private RadioButton falseButton;

    public BooleanConstantFN(double x, double y) {
        super(x, y);
        this.name.set("Boolean Constant");
        this.updateSocketValues();
    }

    @Override
    protected void initSockets() {
        this.output = new Socket<>("Value", true, false);
        this.addSocket(this.output);
    }

    @Override
    public void updateSocketValues() {
        this.output.setValue(this.trueButton.isSelected());
    }

    @Override
    protected void initElements() {
        this.group = new ToggleGroup();

        this.trueButton = new RadioButton("True");
        this.falseButton = new RadioButton("False");

        this.trueButton.setToggleGroup(this.group);
        this.falseButton.setToggleGroup(this.group);

        this.trueButton.setSelected(true);

        HBox hBox = new HBox(10);
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setPadding(new Insets(0, 0, 10, 0));
        hBox.getChildren().addAll(this.trueButton, this.falseButton);

        this.addElement(hBox);

//        this.addElement(trueButton);
//        this.addElement(falseButton);
    }

    @Override
    public void terminateSockets() {
        this.output.terminate();
    }
}
