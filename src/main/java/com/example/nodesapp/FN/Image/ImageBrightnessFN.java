package com.example.nodesapp.FN.Image;

import com.example.nodesapp.FN.FunctionNode;
import com.example.nodesapp.ImageUtilities;
import com.example.nodesapp.Socket;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageBrightnessFN extends FunctionNode {

    Socket<BufferedImage> imageInput;     // input
    Socket<Double> numberInput;     // input
    Socket<BufferedImage> result;     // output

    public ImageBrightnessFN(double x, double y) {
        super(x, y);
        this.name.set("Image Brightness");
    }

    @Override
    protected void initSockets() {
        this.imageInput = new Socket<>("Source image", ImageUtilities.createDefaultImage(300, 300), true);
        this.numberInput = new Socket<>("Brightness factor", 0.0, true);
        this.result = new Socket<>("Result", ImageUtilities.createDefaultImage(300, 300), false);

        this.addAllSockets(this.imageInput, this.numberInput, this.result);
    }

    public void updateSocketValues() {
        BufferedImage image = imageInput.getValue();
        int brightnessFactor = (int) (double) this.numberInput.getValue();

        RescaleOp rescaleOp = new RescaleOp(brightnessFactor, 0, null);
        BufferedImage resultImage = new BufferedImage(
                image.getWidth(), image.getHeight(), image.getType());
        rescaleOp.filter(image, resultImage);

        this.result.setValue(resultImage);
    }

    @Override
    public void terminateSockets() {
        this.imageInput.terminate();
        this.numberInput.terminate();
        this.result.terminate();
    }
}
