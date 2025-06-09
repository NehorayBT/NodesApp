package com.example.nodesapp.FN.Boolean;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;

public class BooleanDisplayFN extends FunctionNode {

    private Socket<Boolean> input;
    private TextFieldUI textField;

    public BooleanDisplayFN(double x, double y){
        super(x, y);
        this.name.set("Boolean display");
        this.updateSocketValues();
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Value", true, true);
        this.addSocket(this.input);
    }

    @Override
    public void updateSocketValues() {
        this.textField.getTextField().setText(this.input.getValue().toString());
    }

    @Override
    protected void initElements() {
        this.textField = new TextFieldUI();

        this.addElement(this.textField);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
    }
}
