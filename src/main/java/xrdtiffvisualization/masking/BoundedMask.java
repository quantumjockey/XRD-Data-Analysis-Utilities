package xrdtiffvisualization.masking;


import javafx.scene.paint.Color;

public class BoundedMask {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int lowerBound;
    private int upperBound;
    private Color maskHue;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public Color getMaskHue() {
        return maskHue;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BoundedMask(int min, int max, Color hue) {
        lowerBound = min;
        upperBound = max;
        maskHue = hue;
    }

}
