package com.example.nodesapp.FN.Image;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.ImageUtilities;
import com.example.nodesapp.Socket;
import com.example.nodesapp.TextFieldUI;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.image.BufferedImage;

public class ImageDisplayFN extends FunctionNode {

    private Socket<BufferedImage> input;
    private ImageView imageView;

    public ImageDisplayFN(double x, double y){
        super(x, y);
        this.name.set("Image display");
        this.updateSocketValues();
    }

    @Override
    protected void initSockets() {
        this.input = new Socket<>("Value", ImageUtilities.createDefaultImage(300, 300), true);
        this.addSocket(this.input);
    }

    @Override
    public void updateSocketValues() {
        Image fxImage = SwingFXUtils.toFXImage(this.input.getValue(), null);
        this.imageView.setImage(fxImage);
        imageView.setFitWidth(150);
    }

    @Override
    protected void initElements() {
        Image fxImage = SwingFXUtils.toFXImage(this.input.getValue(), null);
        this.imageView = new ImageView(fxImage);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);

        HBox hBox = new HBox(10);
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(this.imageView);

        this.addElement(hBox);
    }

    @Override
    public void terminateSockets() {
        this.input.terminate();
    }
}
