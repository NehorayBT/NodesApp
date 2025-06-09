package com.example.nodesapp.FN.Boolean;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;

public class BooleanAndFN extends FunctionNode {

    Socket<Boolean> inputA;     // input A
    Socket<Boolean> inputB;     // input B
    Socket<Boolean> result;     // output

    public BooleanAndFN(double x, double y) {
        super(x, y);
        this.name.set("Boolean And");
    }

    @Override
    protected void initSockets() {
        this.inputA = new Socket<>("Input", false, true);
        this.inputB = new Socket<>("Input", false, true);
        this.result = new Socket<>("Result", false, false);

        this.addAllSockets(this.inputA, this.inputB, this.result);
    }

    public void updateSocketValues() {
        Boolean valA = this.inputA.getValue();
        Boolean valB = this.inputB.getValue();

        this.result.setValue(valA && valB);
    }

    @Override
    public void terminateSockets() {
        this.inputA.terminate();
        this.inputB.terminate();
        this.result.terminate();
    }
}
