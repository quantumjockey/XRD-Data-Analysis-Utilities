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

    public short GetMaxValue(){
        short maxVal = 0;
        for (int i = 0; i < intensityMap[0].length; i++){
            for (int j = 0; j < intensityMap.length; j++){
                if (intensityMap[i][j] > maxVal){
                    maxVal = intensityMap[i][j];
                }
            }
        }
        return maxVal;
    }

}
