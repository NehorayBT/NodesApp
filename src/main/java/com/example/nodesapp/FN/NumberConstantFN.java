package com.example.nodesapp.FN;

import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;

public class NumberConstantFN extends FunctionNode {

    private Socket<Double> output;
    private TextFieldUI textField;

    public NumberConstantFN(double x, double y){
        super(x, y);
        this.name.set("Number constant");
        this.updateSocketValues();
    }

    @Override
    protected void initSockets() {
        this.output = new Socket<Double>("Value", 0.0, false);
        this.addSocket(this.output);
    }

    @Override
    public void updateSocketValues() {
        this.output.setValue(this.textField.getDoubleValue());
    }

    @Override
    protected void initElements() {
        this.textField = new TextFieldUI();
        this.textField.setDecimalOnly(true);
        this.textField.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateSocketValues();
        });

        this.addElement(this.textField);
    }

    @Override
    public void terminateSockets() {
        this.output.terminate();
    }
}
