package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataMasking {

    public static MARTiffImage MaskImage(MARTiffImage image, int maxVal, int minVal){
        MARTiffImage temp = image;
        String name = temp.getFilename().replace('.', '-') + "_mask_" + minVal + "_to_" + maxVal + ".tif";
        temp.setFilename(name);
        for(int y = 0; y < temp.GetHeight(); y++){
            for (int x = 0; x < temp.GetWidth(); x++){
                temp.intensityMap[y][x] = MaskValue(temp.intensityMap[y][x], (short)maxVal, (short)minVal);
            }
        }
        return temp;
    }

    private static short MaskValue(short _value, short _max, short _min){
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
