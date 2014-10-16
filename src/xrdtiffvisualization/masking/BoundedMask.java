package xrdtiffvisualization.masking;


import javafx.scene.paint.Color;

public class BoundedMask {

    public int lowerBound;
    public int upperBound;
    public Color maskHue;

    public BoundedMask(int min, int max, Color hue){
        lowerBound = min;
        upperBound = max;
        maskHue = hue;
    }

    public void updateLowerBound(int lower){
        lowerBound = lower;
    }

    public void updateUpperBound(int upper){
        upperBound = upper;
    }

    public void updateMaskHue(Color hue){
        maskHue = hue;
    }

}
