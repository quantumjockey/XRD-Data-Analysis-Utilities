package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataSubtraction {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage firstImage, MARTiffImage secondImage){

        String filename = firstImage.getFilename().replace('.', '-') + "_minus_" + secondImage.getFilename().replace('.', '-') + ".tif";

        WritableMARTiffImage temp = new WritableMARTiffImage(filename);
        temp.setIfdListing(firstImage.getIfdListing());
        temp.setByteOrder(firstImage.getByteOrder());
        temp.setExcessDataBuffer(firstImage.getExcessDataBuffer());
        int height, width;

        height = (firstImage.getHeight() < secondImage.getHeight()) ? firstImage.getHeight() : secondImage.getHeight();
        width  = (firstImage.getWidth() < secondImage.getWidth()) ? firstImage.getWidth() : secondImage.getWidth();

        temp.initializeIntensityMap(height, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.setIntensityMapCoordinate(y, x, subtractIntensity(firstImage.getIntensityMapValue(y, x), secondImage.getIntensityMapValue(y, x)));
            }
        }
        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static short subtractIntensity(short firstValue, short secondValue){
        return (short)(firstValue - secondValue);
    }

}
