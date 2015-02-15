package xrdtiffvisualization;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import com.quantumjockey.colorramps.GradientRamp;
import xrdtiffvisualization.masking.BoundedMask;
import java.io.IOException;

public class DiffractionFrameVisualizer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final Color[] DEFAULT_RAMP = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; // "Spectrum" Ramp

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MARTiffImage data;
    private int valueOffset;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameVisualizer(MARTiffImage imageData){
        data = imageData;
        valueOffset = scaleImageZero();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataAsImage(GradientRamp _ramp, BoundedMask mask) throws IOException {
        WritableImage displayed = new WritableImage(data.getGeneratedImage().getWidth(), data.getGeneratedImage().getHeight());
        if (mask != null) {
            renderImageWithMask(displayed.getPixelWriter(), data.getGeneratedImage().getMaxValue(), _ramp, mask);
        }
        else{
            renderImageViaColorRamp(displayed.getPixelWriter(), data.getGeneratedImage().getMaxValue(), _ramp);
        }
        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void renderImageViaColorRamp(PixelWriter writer, int maxValue, GradientRamp ramp) throws IOException{
        GradientRamp colorRamp;

        colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        for (int y = 0; y < data.getGeneratedImage().getHeight(); y++) {
            for (int x = 0; x < data.getGeneratedImage().getWidth(); x++) {
                int value = data.getGeneratedImage().getIntensityMapValue(y, x);
                double coefficient = (double) (value + valueOffset) / (double) (maxValue + valueOffset);
                writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
            }
        }
    }

    private void renderImageWithMask(PixelWriter writer, int maxValue, GradientRamp ramp, BoundedMask mask) throws IOException{
        GradientRamp colorRamp;

        colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        for (int y = 0; y < data.getGeneratedImage().getHeight(); y++) {
            for (int x = 0; x < data.getGeneratedImage().getWidth(); x++) {
                int value = data.getGeneratedImage().getIntensityMapValue(y, x);
                if (value < mask.getLowerBound() || value > mask.getUpperBound()){
                    writer.setColor(x, y, mask.getMaskHue());
                }
                else {
                    double coefficient = (double) (value + valueOffset) / (double) (maxValue + valueOffset);
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
                }
            }
        }
    }

    private int scaleImageZero(){
        return Math.abs(data.getGeneratedImage().getMinValue());
    }

}
