package datavisualization.colorramps;

import javafx.scene.paint.Color;

public class RampStop {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    public Color color;
    public double offset;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public RampStop(Color _color, double _offset){
        color = _color;
        offset = _offset;
    }

}
