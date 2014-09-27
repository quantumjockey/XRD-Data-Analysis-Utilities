package xrdtiffvisualization.colorramps;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by quantumjockey on 9/23/14.
 */
public class GradientRamp {

    // Fields
    public ArrayList<RampStop> ramp;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public GradientRamp(Color[] colors) {
        int count = colors.length;
        ramp = new ArrayList<>();
        double unit = 1.0 / ((double)(count - 1));
        for (int i = 0; i < colors.length; i++) {
            ramp.add(new RampStop(colors[i], i * unit));
        }
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public Color getRampColorValue(double scaleVal, double startBoundaryOffset, double finishBoundaryOffset){
        int maxByteValue = 255;
        RampStop firstStop = ramp.get(0);
        RampStop secondStop = ramp.get(ramp.size() - 1);
        for (RampStop boundary : ramp)
        {
            if (boundary.offset <= scaleVal && boundary.offset > startBoundaryOffset)
            {
                firstStop = boundary;
            }
            if (boundary.offset > scaleVal && boundary.offset <= finishBoundaryOffset)
            {
                secondStop = boundary;
                break;
            }
        }
        return Color.rgb(
                CalculateChannelValue(firstStop, secondStop, 'R', scaleVal, maxByteValue),
                CalculateChannelValue(firstStop, secondStop, 'G', scaleVal, maxByteValue),
                CalculateChannelValue(firstStop, secondStop, 'B', scaleVal, maxByteValue)
        );
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private int CalculateChannelValue(RampStop _before, RampStop _after, char _colorComponent, double _offset, int _maxValue) {
        double afterOffset = _after.offset;
        double beforeOffset = _before.offset;
        float max = (float)_maxValue / (float)255;
        double afterColorChannelValue = GetRgbChannelValue(_after.color, _colorComponent);
        double beforeColorChannelValue = GetRgbChannelValue(_before.color, _colorComponent);
        double scaleFactor = (_offset - beforeOffset) / (afterOffset - beforeOffset);
        double channelRange = afterColorChannelValue - beforeColorChannelValue;
        float newChannel = (float)(scaleFactor * channelRange);
        float result = (float)(newChannel + beforeColorChannelValue);
        return (int)(((result < max) ? result : max) * 255);
    }

    private double GetRgbChannelValue(Color _color, char _component){
        double value;
        switch (_component){
            case 'R':
                value = _color.getRed();
                break;
            case 'G':
                value = _color.getGreen();
                break;
            case 'B':
                value = _color.getBlue();
                break;
            default:
                value = _color.getRed();
        }
        return value;
    }

}
