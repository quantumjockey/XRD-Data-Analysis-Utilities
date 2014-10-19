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

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MARTiffImage data;
    private int valueOffset;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffVisualizer(MARTiffImage imageData){
        data = imageData;
        valueOffset = scaleImageZero();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataAsImage(GradientRamp _ramp) throws IOException {
        WritableImage displayed = new WritableImage(data.getWidth(), data.getHeight());
        renderImageViaColorRamp(displayed.getPixelWriter(), data.getMaxValue(), _ramp);
        return displayed;
    }

    public Image renderDataAsImage(GradientRamp _ramp, BoundedMask mask) throws IOException {
        WritableImage displayed = new WritableImage(data.getWidth(), data.getHeight());
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
        for (int y = 0; y < data.getHeight(); y++) {
            for (int x = 0; x < data.getWidth(); x++) {
                int value = data.intensityMap[y][x] + valueOffset;
                if (value < 0) {
                    writer.setColor(x, y, Color.RED);
                }
                else {
                    double coefficient = (double)value / (double)(maxValue + valueOffset);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

    private void renderImageWithMask(PixelWriter writer, short maxValue, GradientRamp ramp, BoundedMask mask) throws IOException{
        GradientRamp colorRamp;
        if (ramp == null) {
            Color[] ramp_colors = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; // "Spectrum" Ramp
            colorRamp = new GradientRamp(ramp_colors);
        }
        else{
            colorRamp = ramp;
        }
        for (int y = 0; y < data.getHeight(); y++) {
            for (int x = 0; x < data.getWidth(); x++) {
                int value = data.intensityMap[y][x] + valueOffset;
                if (value < mask.lowerBound || value > mask.upperBound){
                    writer.setColor(x, y, mask.maskHue);
                }
                else {
                    double coefficient = (double)value / (double)(maxValue + valueOffset);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

    private int scaleImageZero(){
        return Math.abs(data.getMinValue());
    }

}
