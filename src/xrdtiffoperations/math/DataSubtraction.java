package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataSubtraction {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage firstImage, MARTiffImage secondImage){

        String filename = firstImage.getFilename().replace('.', '-') + "_minus_" + secondImage.getFilename().replace('.', '-') + ".tif";

        MARTiffImage temp = new MARTiffImage(filename);
        temp.setIfdListing(firstImage.getIfdListing());
        temp.setByteOrder(firstImage.getByteOrder());
        temp.excessDataBuffer = firstImage.excessDataBuffer;
        int height, width;

        height = (firstImage.getHeight() < secondImage.getHeight()) ? firstImage.getHeight() : secondImage.getHeight();
        width  = (firstImage.getWidth() < secondImage.getWidth()) ? firstImage.getWidth() : secondImage.getWidth();

        temp.intensityMap = new short[height][];

        for (int y = 0; y < height; y++) {
            temp.intensityMap[y] = new short[width];
            for (int x = 0; x < width; x++) {
                temp.intensityMap[y][x] = subtractIntensity(firstImage.intensityMap[y][x], secondImage.intensityMap[y][x]);
            }
        }
        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static short subtractIntensity(short firstValue, short secondValue){
        return (short)(firstValue - secondValue);
    }

}
