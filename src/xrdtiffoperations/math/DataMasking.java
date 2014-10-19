package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataMasking {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage maskImage(MARTiffImage image, int minVal, int maxVal){
        MARTiffImage temp = image;
        String name = temp.filename.replace('.', '-') + "_mask_lb" + minVal + "_ub" + maxVal + ".tif";
        temp.filename = name;
        for(int y = 0; y < temp.getHeight(); y++){
            for (int x = 0; x < temp.getWidth(); x++){
                temp.intensityMap[y][x] = maskValue(temp.intensityMap[y][x], (short) maxVal, (short) minVal);
            }
        }
        return temp;
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private static short maskValue(short _value, short _max, short _min){
        short value = _value;
        if (value < _min){
            value = _min;
        }
        else if (value > _max){
            value = _max;
        }
        return value;
    }

}
