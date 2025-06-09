package com.example.nodesapp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtilities {
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                source.getType()
        );
        copy.setData(source.getData());
        return copy;
    }

    public static BufferedImage createDefaultImage(int width, int height) {
        // Create a BufferedImage with RGB color model
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Fill with black color
        Color black = new Color(0, 0, 0);
        int blackRGB = black.getRGB();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, blackRGB);
            }
        }
        return image;
    }
}
