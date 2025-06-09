package com.example.nodesapp.FN.Converters;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;

public class ConverterNumericalToBooleanFN extends FunctionNode {

    Socket<Double> input;     // input
    Socket<Boolean> result;     // output

    public ConverterNumericalToBooleanFN(double x, double y) {
        super(x, y);
        this.name.set("Numerical to Boolean Converter");
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Input", 0.0, true);
        this.result = new Socket<>("Output", false, false);

        this.addAllSockets(this.input, this.result);
    }

    public void updateSocketValues() {
        Double inputVal = this.input.getValue();
        Boolean outputVal = inputVal != 0;

        this.result.setValue(outputVal);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
        this.result.terminate();
    }
}
