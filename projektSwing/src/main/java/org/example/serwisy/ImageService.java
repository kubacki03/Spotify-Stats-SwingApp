package org.example.serwisy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageService {

    public static BufferedImage getImage(String imageUrl) throws IOException {
        BufferedImage image = ImageIO.read(new URL(imageUrl));
        return (image);
    }

}
