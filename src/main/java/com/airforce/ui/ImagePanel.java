package com.airforce.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel() {
        image = ImageLoader.loadImage("/noplayerimage.png");
    }

    public void setImage(boolean isExistPlayer) {
        if (isExistPlayer) {
            image = ImageLoader.loadImage("/pilot.png");
//                    .getSubimage(5, 10, 290, 240);
        } else {
            image = ImageLoader.loadImage("/noplayerimage.png");
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 160, 170,this); // see javadoc for more info on the parameters
    }
}
