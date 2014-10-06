package app.ImageManipulation.math;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageSubtractor {


    public BufferedImage SubtractImages(BufferedImage firstImage, BufferedImage secondImage) {

        int red, green, blue;
        Color firstPixel, secondPixel;
        int height, width;

        height = (firstImage.getHeight() < secondImage.getHeight()) ? firstImage.getHeight() : secondImage.getHeight();
        width  = (firstImage.getWidth() < secondImage.getWidth()) ? firstImage.getWidth() : secondImage.getWidth();

        BufferedImage temp = new BufferedImage(width, height, firstImage.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                firstPixel = new Color(firstImage.getRGB(x, y));
                secondPixel = new Color(secondImage.getRGB(x, y));

                red = SubtractColorChannel(firstPixel.getRed(), secondPixel.getRed());
                green = SubtractColorChannel(firstPixel.getGreen(), secondPixel.getGreen());
                blue = SubtractColorChannel(firstPixel.getBlue(), secondPixel.getBlue());

                temp.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return temp;
    }


    ////// NEEDS WORK //////
    public BufferedImage SubtractImagesWithAlpha(BufferedImage firstImage, BufferedImage secondImage) {

        int red, green, blue, alpha;
        Color firstPixel, secondPixel;
        int height, width;
        height = (firstImage.getHeight() < secondImage.getHeight()) ? firstImage.getHeight() : secondImage.getHeight();
        width  = (firstImage.getWidth() < secondImage.getWidth()) ? firstImage.getWidth() : secondImage.getWidth();

        BufferedImage temp = new BufferedImage(width, height, firstImage.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                firstPixel = new Color(firstImage.getRGB(x, y));
                secondPixel = new Color(secondImage.getRGB(x, y));

                red = SubtractColorChannel(firstPixel.getRed(), secondPixel.getRed());
                green = SubtractColorChannel(firstPixel.getGreen(), secondPixel.getGreen());
                blue = SubtractColorChannel(firstPixel.getBlue(), secondPixel.getBlue());

                temp.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return temp;
    }
    ///// NEEDS WORK /////


    private int SubtractColorChannel(int firstChannelValue, int secondChannelValue){
        int value = secondChannelValue - firstChannelValue;
        if (value < 0){
            value = 0;
        }
        return value;
    }

}
