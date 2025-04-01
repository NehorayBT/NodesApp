package com.example.nodesapp;

public class IntegerConstantFN extends FunctionNode{
    private int value;

    public IntegerConstantFN(double x, double y, int value){
        super(x, y);
        this.value = value;
    }

    @Override
    protected void initSockets() {
        SocketValue val = SocketValue.createInteger(this.value, this.value);
        Socket output = new Socket("Value", val, false);
        this.addOutputSocket(output);
    }

    @Override
    protected void updateSocketValues() {
        this.outputSockets.get(0).getValue().setValue(this.value);
    }
}
