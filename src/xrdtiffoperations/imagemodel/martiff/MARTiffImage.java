package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.TiffBase;

/**
 * Created by quantumjockey on 9/18/14.
 */
public class MARTiffImage extends TiffBase{

    // Image data
    public short[][] intensityMap;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short GetMaxValue(){
        short maxVal = 0;
        for (int y = 0; y < GetHeight(); y++){
            for (int x = 0; x < GetWidth(); x++){
                if (intensityMap[y][x] > maxVal){
                    maxVal = intensityMap[y][x];
                }
            }
        }
        return maxVal;
    }

    public short GetMinValue(){
        short minVal = 0;
        for (int y = 0; y < GetHeight(); y++){
            for (int x = 0; x < GetWidth(); x++){
                if (intensityMap[y][x] < minVal){
                    minVal = intensityMap[y][x];
                }
            }
        }
        return minVal;
    }

    public int GetHeight(){
        return intensityMap.length;
    }

    public int GetWidth(){
        return intensityMap[0].length;
    }

}
