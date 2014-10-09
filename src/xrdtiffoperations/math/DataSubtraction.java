package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataSubtraction {

    public static MARTiffImage SubtractImages(MARTiffImage firstImage, MARTiffImage secondImage, boolean isAbsolute){

        String filename = firstImage.getFilename().replace('.', '-') + "_minus_" + secondImage.getFilename().replace('.', '-') + ".tif";

        MARTiffImage temp = new MARTiffImage(filename);
        temp.ifdListing = firstImage.ifdListing;
        temp.byteOrder = firstImage.byteOrder;
        int height, width;

        height = (firstImage.intensityMap.length < secondImage.intensityMap.length) ? firstImage.intensityMap.length : secondImage.intensityMap.length;
        width  = (firstImage.intensityMap[0].length < secondImage.intensityMap[0].length) ? firstImage.intensityMap[0].length : secondImage.intensityMap[0].length;

        temp.intensityMap = new short[height][];

        for (int x = 0; x < width; x++) {
            temp.intensityMap[x] = new short[width];
            for (int y = 0; y < height; y++) {
                temp.intensityMap[x][y] = SubtractIntensity(firstImage.intensityMap[x][y], secondImage.intensityMap[x][y], isAbsolute);
            }
        }
        return temp;
    }

    private static short SubtractIntensity(short firstValue, short secondValue, boolean absolute){
        short value;
        if (absolute){
            value = (short) Math.abs(firstValue - secondValue);
        } else {
            value = (short)(firstValue - secondValue);
        }
        return value;
    }

}
