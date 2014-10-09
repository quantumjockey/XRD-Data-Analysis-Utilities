package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class DataMasking {

    public static MARTiffImage MaskImage(MARTiffImage image, int maxVal, int minVal){
        MARTiffImage temp = image;
        String name = temp.getFilename().replace('.', '-') + "_mask_" + minVal + "_to_" + maxVal + ".tif";
        temp.setFilename(name);
        for(int y = 0; y < temp.intensityMap.length; y++){
            for (int x = 0; x < temp.intensityMap[y].length; x++){
                short value = temp.intensityMap[x][y];
                if (value < minVal){
                    value = (short)minVal;
                }
                if (value > maxVal){
                    value = (short)maxVal;
                }
                temp.intensityMap[x][y] = value;
            }
        }
        return temp;
    }

}
