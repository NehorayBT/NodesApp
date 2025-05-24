package com.example.nodesapp;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TextFieldUI extends HBox {

    private TextField textField;
    private ChangeListener<String> decimalOnly;

    public TextFieldUI() {
        this.textField = new TextField();
        this.initDecimalOnly();
        drawElement();
    }

    private void drawElement() {
        this.setPadding(new Insets(10));

        this.textField = new TextField("0");
        this.textField.setBackground(new Background(new BackgroundFill(Color.web("#333333"), null, null)));
        this.textField.setStyle("-fx-text-fill: white;");

        this.getChildren().add(textField);
    }

    protected void initDecimalOnly() {
        this.decimalOnly = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[+-]?\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);  // Revert to the last valid value
            }
            if(newValue.isEmpty()) {
                textField.setText("0");
            }
        };
    }

    public void setDecimalOnly(boolean isDecimal) {
        if(isDecimal) {
            this.textField.textProperty().addListener(this.decimalOnly);
        } else {
            this.textField.textProperty().removeListener(this.decimalOnly);
        }
    }

    public TextField getTextField() {
        return this.textField;
    }

    public Double getDoubleValue() {
        if(this.textField.getText().matches("[+-]?\\d*(\\.\\d*)?") && (!this.textField.getText().equals("-"))) {
            return Double.valueOf(this.textField.getText());
        }
        return 0.0;
    }

}


