package edu.hipsec.xrddau.xrdvisualization.masking;


import javafx.scene.paint.Color;

public class BoundedMask {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int lowerBound;
    private int upperBound;
    private Color maskHue;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public int getLowerBound() {
        return this.lowerBound;
    }

    public int getUpperBound() {
        return this.upperBound;
    }

    public Color getMaskHue() {
        return this.maskHue;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BoundedMask(int min, int max, Color hue) {
        this.lowerBound = min;
        this.upperBound = max;
        this.maskHue = hue;
    }

}
