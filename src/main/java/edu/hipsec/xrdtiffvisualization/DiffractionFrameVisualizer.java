package edu.hipsec.xrdtiffvisualization;

import edu.hipsec.xrdtiffoperations.datamapping.TwoDimensionalDoubleMapping;
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
        this.valueOffset = this.data.scaleImageZero();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataAsFalseColorMapping(GradientRamp _ramp, BoundedMask mask, boolean adaptive) throws IOException {
        WritableImage displayed = new WritableImage(this.data.getWidth(), this.data.getHeight());

        if (mask != null)
            this.renderImageWithMask(displayed.getPixelWriter(), this.data.getMaxValue(), _ramp, mask, adaptive);
        else
            this.renderImageViaColorRamp(displayed.getPixelWriter(), this.data.getMaxValue(), _ramp);

        return displayed;
    }

    public Image renderDataAsGradientMapping(GradientRamp ramp) {
        WritableImage displayed = new WritableImage(this.data.getWidth(), this.data.getHeight());
        GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        TwoDimensionalDoubleMapping gradientMapping
                = new TwoDimensionalDoubleMapping(this.data.getHeight(), this.data.getWidth());

        this.data.cycleFramePixels((y, x) ->
                gradientMapping.setMapCoordinate(y, x, discernAverageIntensityShift(y, x)));

        double maxValue = gradientMapping.getMaxValue();

        gradientMapping.cycleMap((y, x) -> {
            double coefficient = (gradientMapping.get(y,x) + this.valueOffset) / (maxValue + this.valueOffset);
            displayed.getPixelWriter().setColor(x, y, colorRamp.getRampColorValue(coefficient));
        });

        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private double discernAverageIntensityShift(int y, int x) {
        int primarySum = 0;
        int baseVal = this.data.getIntensityMapValue(y, x);

        for (int i = y - 1; i <= y + 1; i++)
            for (int j = x - 1; j <= x + 1; j++)
                if (i < this.data.getHeight() && i >= 0 && j < this.data.getWidth() && j >= 0)
                    primarySum += this.data.getIntensityMapValue(i, j) - baseVal;

        return (double) primarySum / 8.0;
    }

    private void renderImageViaColorRamp(PixelWriter writer, int maxValue, GradientRamp ramp) throws IOException {
        GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        this.data.cycleFramePixels((y, x) -> {
            int value = this.data.getIntensityMapValue(y, x);
            double coefficient = (double) (value + this.valueOffset) / (double) (maxValue + this.valueOffset);
            writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
        });
    }

    private void renderImageWithMask(PixelWriter writer, int maxValue, GradientRamp ramp, BoundedMask mask, boolean adaptive) throws IOException {
        GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(DEFAULT_RAMP)) : ramp;

        this.valueOffset = (adaptive) ? mask.getLowerBound() : this.data.scaleImageZero();

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

}
