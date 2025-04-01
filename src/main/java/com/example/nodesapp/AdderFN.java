package com.example.nodesapp;

public class AdderFN extends FunctionNode{
    public AdderFN(double x, double y) {
        super(x, y);
    }

    @Override
    protected void initSockets() {
        SocketValue valA = SocketValue.createInteger(0, 0);
        SocketValue valB = SocketValue.createInteger(0, 0);
        SocketValue valResult = SocketValue.createInteger(0, 0);

        Socket inputA = new Socket("Value A", valA, true);
        Socket inputB = new Socket("Value B", valB, true);
        Socket result = new Socket("Result", valResult, false);

        addInputSocket(inputA);
        addInputSocket(inputB);
        addOutputSocket(result);
    }

    protected void updateSocketValues() {
        int valA = this.inputSockets.get(0).getValue().getAsInteger();
        int valB = this.inputSockets.get(1).getValue().getAsInteger();
        int result = valA + valB;

        this.outputSockets.get(0).getValue().setValue(result);
    }
}
