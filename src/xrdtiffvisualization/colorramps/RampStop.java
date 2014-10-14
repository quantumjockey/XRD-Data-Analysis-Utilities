package xrdtiffvisualization.colorramps;

import javafx.scene.paint.Color;

public class RampStop {

    public Color color;
    public double offset;

    public RampStop(Color _color, double _offset){
        color = _color;
        offset = _offset;
    }
}
