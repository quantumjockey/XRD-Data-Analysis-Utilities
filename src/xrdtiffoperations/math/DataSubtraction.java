package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataSubtraction {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage firstImage, MARTiffImage secondImage){

        String filename = firstImage.filename.replace('.', '-') + "_minus_" + secondImage.filename.replace('.', '-') + ".tif";

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
                temp.intensityMap[x][y] = subtractIntensity(firstImage.intensityMap[x][y], secondImage.intensityMap[x][y]);
            }
        }
        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static short subtractIntensity(short firstValue, short secondValue){
        return (short)(firstValue - secondValue);
    }

}
