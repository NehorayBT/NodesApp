package com.example.nodesapp.FN.Numerical;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;

public class NumericalDisplayFN extends FunctionNode {

    private Socket<Double> input;
    private TextFieldUI textField;

    public NumericalDisplayFN(double x, double y){
        super(x, y);
        this.name.set("Number display");
        this.updateSocketValues();
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Value", 0.0, true);
        this.addSocket(this.input);
    }

    @Override
    public void updateSocketValues() {
        this.textField.getTextField().setText(this.input.getValue().toString());
    }

    @Override
    protected void initElements() {
        this.textField = new TextFieldUI();
        this.textField.setDecimalOnly(true);

        this.addElement(this.textField);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
    }
}
