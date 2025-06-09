package com.example.nodesapp.FN.Numerical;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class NumericalConstantFN extends FunctionNode {

    private Socket<Double> output;
    private TextFieldUI minRangeTextField;
    private TextFieldUI targetTextField;
    private TextFieldUI maxRangeTextField;
    private Slider slider;

    public NumericalConstantFN(double x, double y){
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
        this.output.setValue(this.targetTextField.getDoubleValue());
    }

    private double getMinValue() {
        String strVal = this.minRangeTextField.getTextField().getText();
        if(strVal.equals("-")) {
            return 0;
        }
        return Double.parseDouble(strVal);
    }

    private double getTargetValue() {
        String strVal = this.targetTextField.getTextField().getText();
        if(strVal.equals("-")) {
            return 0;
        }
        return Double.parseDouble(strVal);
    }

    private double getMaxValue() {
        String strVal = this.maxRangeTextField.getTextField().getText();
        if(strVal.equals("-")) {
            return 0;
        }
        return Double.parseDouble(strVal);
    }

    @Override
    protected void initElements() {
        this.slider = new Slider();
        this.slider.setMin(-100);
        this.slider.setMax(100);
        this.slider.setValue(0);
        this.slider.setMaxWidth(280);
        this.slider.setMinWidth(280);
        this.slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.targetTextField.getTextField().setText(String.valueOf(newValue));
        });

        this.minRangeTextField = new TextFieldUI();
        this.targetTextField = new TextFieldUI();
        this.maxRangeTextField = new TextFieldUI();

        this.minRangeTextField.setDecimalOnly(true);
        this.targetTextField.setDecimalOnly(true);
        this.maxRangeTextField.setDecimalOnly(true);

        this.minRangeTextField.getTextField().setText("-100");
        this.maxRangeTextField.getTextField().setText("100");

        this.minRangeTextField.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            this.slider.setMin(this.getMinValue());
            if(this.getMinValue() > this.getTargetValue()) {
                this.slider.setValue(this.getMinValue());
            }
            if(this.getMinValue() >= this.getMaxValue()) {
                double newMax = this.getMinValue() + 100;
                this.slider.setMax(newMax);
                this.maxRangeTextField.getTextField().setText(String.valueOf(newMax));
            }
            this.slider.setMin(this.getMinValue());
        });
        this.targetTextField.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateSocketValues();
            this.slider.setValue(this.getTargetValue());
        });
        this.maxRangeTextField.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            if(this.getMaxValue() < this.getTargetValue()) {
                this.slider.setValue(this.getMaxValue());
            }
            if(this.getMaxValue() <= this.getMinValue()) {
                double newMin = this.getMaxValue() - 100;
                this.slider.setMin(newMin);
                this.minRangeTextField.getTextField().setText(String.valueOf(newMin));
            }
            this.slider.setMax(this.getMaxValue());
        });

        HBox textBoxesHBox = new HBox(10);
        textBoxesHBox.setAlignment(javafx.geometry.Pos.CENTER);
        textBoxesHBox.setPadding(new Insets(10));
        textBoxesHBox.getChildren().addAll(this.minRangeTextField, this.targetTextField, this.maxRangeTextField);

        HBox sliderHBox = new HBox(10);
        sliderHBox.setAlignment(javafx.geometry.Pos.CENTER);
        sliderHBox.setPadding(new Insets(10));
        sliderHBox.getChildren().addAll(this.slider);

        this.addElement(sliderHBox);
        this.addElement(textBoxesHBox);
    }

    @Override
    public void terminateSockets() {
        this.output.terminate();
    }
}
