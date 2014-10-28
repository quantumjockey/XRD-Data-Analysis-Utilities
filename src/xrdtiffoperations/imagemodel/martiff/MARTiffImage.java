package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.TiffBase;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;

public class MARTiffImage extends TiffBase{

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final short INTENSITY_MAXIMUM = 32767;
    private final short INTENSITY_MINIMUM = -32768;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected CalibrationData calibration;
    protected short[][] intensityMap;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public CalibrationData getCalibration(){
        return calibration;
    }

    public short getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short getMaxValue(){
        short maxVal = INTENSITY_MINIMUM;
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
        short minVal = INTENSITY_MAXIMUM;
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
