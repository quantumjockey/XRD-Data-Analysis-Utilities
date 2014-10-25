package xrdtiffoperations.visualization;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import colorramps.GradientRamp;
import xrdtiffoperations.visualization.masking.BoundedMask;

import java.io.IOException;

public class MARTiffVisualizer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final Color[] DEFAULT_RAMP = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; // "Spectrum" Ramp

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
            colorRamp = new GradientRamp(DEFAULT_RAMP);
        }
        else{
            colorRamp = ramp;
        }
        for (int y = 0; y < data.getHeight(); y++) {
            for (int x = 0; x < data.getWidth(); x++) {
                int value = data.intensityMap[y][x];
                double coefficient = (double)(value + valueOffset) / (double)(maxValue + valueOffset);
                writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
            }
        }
    }

    private void renderImageWithMask(PixelWriter writer, short maxValue, GradientRamp ramp, BoundedMask mask) throws IOException{
        GradientRamp colorRamp;
        if (ramp == null) {
            colorRamp = new GradientRamp(DEFAULT_RAMP);
        }
        else{
            colorRamp = ramp;
        }
        for (int y = 0; y < data.getHeight(); y++) {
            for (int x = 0; x < data.getWidth(); x++) {
                int value = data.intensityMap[y][x];
                if (value < mask.getLowerBound() || value > mask.getUpperBound()){
                    writer.setColor(x, y, mask.getMaskHue());
                }
                else {
                    double coefficient = (double)(value + valueOffset) / (double)(maxValue + valueOffset);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

    private int scaleImageZero(){
        return Math.abs(data.getMinValue());
    }

}
