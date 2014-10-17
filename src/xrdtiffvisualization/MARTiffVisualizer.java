package xrdtiffvisualization;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffvisualization.colorramps.GradientRamp;
import xrdtiffvisualization.masking.BoundedMask;

import java.io.IOException;

public class MARTiffVisualizer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int VALUE_OFFSET = 32768;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MARTiffImage data;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffVisualizer(MARTiffImage imageData){
        data = imageData;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataAsImage(GradientRamp _ramp) throws IOException {
        WritableImage displayed = new WritableImage(data.intensityMap[0].length, data.intensityMap.length);
        renderImageViaColorRamp(displayed.getPixelWriter(), data.getMaxValue(), _ramp);
        return displayed;
    }

    public Image renderDataAsImage(GradientRamp _ramp, BoundedMask mask) throws IOException {
        WritableImage displayed = new WritableImage(data.intensityMap[0].length, data.intensityMap.length);
        renderImageWithMask(displayed.getPixelWriter(), data.getMaxValue(), _ramp, mask);
        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void renderImageViaColorRamp(PixelWriter writer, short maxValue, GradientRamp ramp) throws IOException{
        GradientRamp colorRamp;
        if (ramp == null) {
            Color[] ramp_colors = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}; // "Spectrum" Ramp
            colorRamp = new GradientRamp(ramp_colors);
        }
        else{
            colorRamp = ramp;
        }
        for (int x = 0; x < data.intensityMap[0].length; x++) {
            for (int y = 0; y < data.intensityMap.length; y++) {
                int value = data.intensityMap[y][x] + VALUE_OFFSET;
                if (value < 0) {
                    writer.setColor(x, y, Color.RED);
                }
                else {
                    double coefficient = (double)value / (double)(maxValue + VALUE_OFFSET);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

    private void renderImageWithMask(PixelWriter writer, short maxValue, GradientRamp ramp, BoundedMask mask) throws IOException{
        GradientRamp colorRamp;
        if (ramp == null) {
            Color[] ramp_colors = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}; // "Spectrum" Ramp
            colorRamp = new GradientRamp(ramp_colors);
        }
        else{
            colorRamp = ramp;
        }
        for (int x = 0; x < data.intensityMap[0].length; x++) {
            for (int y = 0; y < data.intensityMap.length; y++) {
                int value = data.intensityMap[y][x] + VALUE_OFFSET;
                if (value < mask.lowerBound || value > mask.upperBound){
                    writer.setColor(x, y, mask.maskHue);
                }
                else {
                    double coefficient = (double)value / (double)(maxValue + VALUE_OFFSET);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

}
