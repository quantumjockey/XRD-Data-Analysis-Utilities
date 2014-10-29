package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataSubtraction {

    /////////// Constants /////////////////////////////////////////////////////////////////////

    private static final String DEFAULT_EXTENSION = ".tif";

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage firstImage, MARTiffImage secondImage){

        String filename = generateFilename(firstImage, secondImage, false);

        WritableMARTiffImage temp = new WritableMARTiffImage(filename);
        temp.setIfdListing(firstImage.getIfdListing());
        temp.setByteOrder(firstImage.getByteOrder());
        temp.setCalibration(firstImage.getCalibration());
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

    private static String generateFilename(MARTiffImage firstFile, MARTiffImage secondFile, boolean longName){
        String result;
        String firstSegment = stripFilename(firstFile.getFilename());
        String secondSegment = stripFilename(secondFile.getFilename());
        if (longName){
            result = firstSegment + "_minus_" + secondSegment + DEFAULT_EXTENSION;
        }
        else{
            String[] suffixParts = secondSegment.split("_");
            result = firstSegment + "_bknd_" + suffixParts[suffixParts.length - 1] + DEFAULT_EXTENSION;
        }
        return result;
    }

    private static String stripFilename(String name){
        return name.replace(DEFAULT_EXTENSION, "");
    }

    private static short subtractIntensity(short firstValue, short secondValue){
        return (short)(firstValue - secondValue);
    }

}
