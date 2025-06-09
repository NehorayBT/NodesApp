package com.example.nodesapp.FN.Image;

import com.example.nodesapp.Director;
import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.ImageUtilities;
import com.example.nodesapp.Socket;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageConstantFN extends FunctionNode {

    private Socket<BufferedImage> output;

    private Label fileLabel;
    private Button chooseButton;
    private BufferedImage image;

    public ImageConstantFN(double x, double y) {
        super(x, y);
        this.name.set("Image Constant");
        this.updateSocketValues();
    }



    @Override
    protected void initSockets() {
        this.image = ImageUtilities.createDefaultImage(300, 200);

        this.output = new Socket<>("Value", this.image, false);
        this.addSocket(this.output);
    }

    @Override
    public void updateSocketValues() {
        this.output.setValue(ImageUtilities.copyImage(this.image));
    }

    @Override
    protected void initElements() {
        this.fileLabel = new Label("No file selected");
        this.chooseButton = new Button("Choose Image");

        chooseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );

            File selectedFile = fileChooser.showOpenDialog(Director.getInstance().getStage());
            if (selectedFile != null) {
                String fileName = selectedFile.getName();
                fileName = fileName.length() <= 10 ? fileName : fileName.substring(0, 10) + "..";
                fileLabel.setText(fileName);
                try {
                    this.image = ImageIO.read(selectedFile);
                    this.updateSocketValues();
                } catch(Exception error) {
                    return;
                }
            } else {
                fileLabel.setText("No file selected");
            }
        });

        HBox hBox = new HBox(10);
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(this.chooseButton, this.fileLabel);

        this.addElement(hBox);
    }

    @Override
    public void terminateSockets() {
        this.output.terminate();
    }
}
