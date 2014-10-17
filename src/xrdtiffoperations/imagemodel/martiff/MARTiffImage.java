package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.TiffBase;

public class MARTiffImage extends TiffBase{

    // Constants
    private final int RAW_VALUE_OFFSET = 32768;
    private final int INTENSITY_MAXIMUM = 65536;
    private final int INTENSITY_MINIMUM = 0;

    // Image data
    public short[][] intensityMap;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int getOffsetMaxValue(){
        int maxVal = INTENSITY_MINIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] + RAW_VALUE_OFFSET > maxVal){
                    maxVal = intensityMap[y][x] + RAW_VALUE_OFFSET;
                }
            }
        }
        return maxVal;
    }

    public int getOffsetMinValue(){
        int minVal = INTENSITY_MAXIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] + RAW_VALUE_OFFSET < minVal){
                    minVal = intensityMap[y][x] + RAW_VALUE_OFFSET;
                }
            }
        }
        return minVal;
    }

    public short getMaxValue(){
        short maxVal = 0;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] > maxVal){
                    maxVal = intensityMap[y][x];
                }
            }
        }
        return maxVal;
    }

    public short getMinValue(){
        short minVal = 0;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] < minVal){
                    minVal = intensityMap[y][x];
                }
            }
        }
        return minVal;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

}
