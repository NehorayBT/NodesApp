package com.example.nodesapp.FN.Converters;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;

public class ConverterBooleanToNumericalFN extends FunctionNode {

    Socket<Boolean> input;     // input
    Socket<Double> result;     // output

    public ConverterBooleanToNumericalFN(double x, double y) {
        super(x, y);
        this.name.set("Boolean to Numerical Converter");
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Input", false, true);
        this.result = new Socket<>("Output", 0.0, false);

        this.addAllSockets(this.input, this.result);
    }

    public void updateSocketValues() {
        Boolean inputVal = this.input.getValue();
        Double outputVal = inputVal ? 1.0 : 0.0;

        this.result.setValue(outputVal);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
        this.result.terminate();
    }
}
