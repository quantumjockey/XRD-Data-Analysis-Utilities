package xrdtiffvisualization.colorramps;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by quantumjockey on 9/23/14.
 */
public class GradientRamp {

    public ArrayList<RampStop> ramp;

    public GradientRamp(Color[] colors) {
        int count = colors.length;
        ramp = new ArrayList<>();

        double unit = 1.0 / ((double)(count - 1));

        for (int i = 0; i < colors.length; i++) {
            ramp.add(new RampStop(colors[i], i * unit));
        }
    }

    public Color getRampColorValue(double scaleVal){
        double startBoundaryOffset = 0.0;
        double finishBoundaryOffset = 1.0;
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
                (int)CalculateChannelValue(firstStop, secondStop, 'R', scaleVal, maxByteValue),
                (int)CalculateChannelValue(firstStop, secondStop, 'G', scaleVal, maxByteValue),
                (int)CalculateChannelValue(firstStop, secondStop, 'B', scaleVal, maxByteValue)
        );
    }


    private float CalculateChannelValue(RampStop _before, RampStop _after, char _colorComponent, double _offset, int _maxValue) {
        double afterOffset = _after.offset;
        double beforeOffset = _before.offset;
        float max = (float)_maxValue / (float)255;
        double afterColorChannelValue = GetRgbChannelValue(_after.color, _colorComponent);
        double beforeColorChannelValue = GetRgbChannelValue(_before.color, _colorComponent);
        double scaleFactor = (_offset - beforeOffset) / (afterOffset - beforeOffset);
        double channelRange = afterColorChannelValue - beforeColorChannelValue;
        float newChannel = (float)(scaleFactor * channelRange);
        float result = (float)(newChannel + beforeColorChannelValue);
        return (result < max) ? result : max;
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
