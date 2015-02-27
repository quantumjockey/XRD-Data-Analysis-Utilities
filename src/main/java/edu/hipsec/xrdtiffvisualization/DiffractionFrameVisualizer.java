package edu.hipsec.xrdtiffvisualization;

import edu.hipsec.xrdtiffvisualization.masking.BoundedMask;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import com.quantumjockey.colorramps.GradientRamp;
import java.io.IOException;

public class DiffractionFrameVisualizer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final Color[] DEFAULT_RAMP = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; // "Spectrum" Ramp

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private DiffractionFrame data;
    private int valueOffset;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameVisualizer(DiffractionFrame imageData) {
        this.data = imageData;
        this.valueOffset = scaleImageZero();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataAsImage(GradientRamp _ramp, BoundedMask mask, boolean adaptive) throws IOException {
        WritableImage displayed = new WritableImage(this.data.getWidth(), this.data.getHeight());

        if (mask != null)
            renderImageWithMask(displayed.getPixelWriter(), this.data.getMaxValue(), _ramp, mask, adaptive);
        else
            renderImageViaColorRamp(displayed.getPixelWriter(), this.data.getMaxValue(), _ramp);

        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void renderImageViaColorRamp(PixelWriter writer, int maxValue, GradientRamp ramp) throws IOException {
        GradientRamp colorRamp;

        colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        this.data.cycleFramePixels((y, x) -> {
            int value = this.data.getIntensityMapValue(y, x);
            double coefficient = (double) (value + this.valueOffset) / (double) (maxValue + this.valueOffset);
            writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
        });
    }

    private void renderImageWithMask(PixelWriter writer, int maxValue, GradientRamp ramp, BoundedMask mask, boolean adaptive) throws IOException {
        GradientRamp colorRamp;

        colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        this.valueOffset = (adaptive) ? mask.getLowerBound() : scaleImageZero();

        this.data.cycleFramePixels((y, x) -> {
            int value = this.data.getIntensityMapValue(y, x);
            if (value < mask.getLowerBound() || value > mask.getUpperBound()) {
                writer.setColor(x, y, mask.getMaskHue());
            } else {
                double coefficient = (double) (value + this.valueOffset) / (double) (maxValue + this.valueOffset);
                writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
            }
        });
    }

    private int scaleImageZero() {
        return Math.abs(this.data.getMinValue());
    }

}
