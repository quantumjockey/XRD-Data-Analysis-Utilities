package xrdtiffvisualization.masking;


import javafx.scene.paint.Color;

public class BoundedMask {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    public int lowerBound;
    public int upperBound;
    public Color maskHue;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BoundedMask(int min, int max, Color hue){
        lowerBound = min;
        upperBound = max;
        maskHue = hue;
    }

}
