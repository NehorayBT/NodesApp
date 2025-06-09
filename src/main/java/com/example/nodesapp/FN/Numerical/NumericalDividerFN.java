package com.example.nodesapp.FN.Numerical;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;

public class NumericalDividerFN extends FunctionNode {

    Socket<Double> inputA;     // input
    Socket<Double> inputB;     // input
    Socket<Double> result;     // output

    public NumericalDividerFN(double x, double y) {
        super(x, y);
        this.name.set("Numerical Divider");
    }

    @Override
    protected void initSockets() {
        this.inputA = new Socket<>("Input A", 0.0, true);
        this.inputB = new Socket<>("Input B", 0.0, true);
        this.result = new Socket<>("Result", 0.0, false);

        this.addAllSockets(this.inputA, this.inputB, this.result);
    }

    public void updateSocketValues() {
        double valA = this.inputA.getValue();
        double valB = this.inputB.getValue();
        double res = valA / valB;

        this.result.setValue(res);
    }

    @Override
    public void terminateSockets() {
        this.inputA.terminate();
        this.inputB.terminate();
        this.result.terminate();
    }
}
