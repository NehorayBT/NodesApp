package com.example.nodesapp.FN.Boolean;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;

public class BooleanNotFN extends FunctionNode {

    Socket<Boolean> input;     // input
    Socket<Boolean> result;     // output

    public BooleanNotFN(double x, double y) {
        super(x, y);
        this.name.set("Boolean Not");
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Input", false, true);
        this.result = new Socket<>("Result", true, false);

        this.addAllSockets(this.input, this.result);
    }

    public void updateSocketValues() {
        Boolean inputVal = this.input.getValue();
        this.result.setValue(!inputVal);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
        this.result.terminate();
    }
}
