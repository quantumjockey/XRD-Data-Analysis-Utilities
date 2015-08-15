package edu.hipsec.xrddau.xrdvisualization.engine;

import com.quantumjockey.colorramps.GradientRamp;
import edu.hipsec.concurrency.BackgroundTask;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.data.mapping.TwoDimensionalDoubleMapping;
import edu.hipsec.xrddau.xrdvisualization.engine.base.DataMappingEngine;
import edu.hipsec.xrddau.xrdvisualization.masking.BoundedMask;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GradientMappingEngine extends DataMappingEngine {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Image renderData(DiffractionFrame data, GradientRamp ramp, BoundedMask mask, boolean adaptive) {
        WritableImage displayed = new WritableImage(data.getWidth(), data.getHeight());
        this.renderImageViaGradientMap(data, displayed.getPixelWriter(), ramp);
        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private double discernAverageIntensityShift(DiffractionFrame data, int y, int x) {
        int primarySum = 0;
        int baseVal = data.getIntensityMapValue(y, x);

        for (int i = y - 1; i <= y + 1; i++)
            for (int j = x - 1; j <= x + 1; j++)
                if (i < data.getHeight() && i >= 0 && j < data.getWidth() && j >= 0)
                    primarySum += data.getIntensityMapValue(i, j) - baseVal;

        return (double) primarySum / 8.0;
    }

    private void renderImageViaGradientMap(DiffractionFrame data, PixelWriter writer, GradientRamp ramp) {
        BackgroundTask.execute(() -> {
            GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(this.DEFAULT_RAMP)) : ramp;
            final int zeroOffset = data.scaleImageZero();

            TwoDimensionalDoubleMapping gradientMapping = new TwoDimensionalDoubleMapping(data.getHeight(), data.getWidth());

            data.cycleFramePixels((y, x) -> gradientMapping.setMapCoordinate(y, x, this.discernAverageIntensityShift(data, y, x)));

            double maxValue = gradientMapping.getDynamicMaxValue();

            gradientMapping.cycleMap((y, x) -> {
                double coefficient = (gradientMapping.get(y, x) + zeroOffset) / (maxValue + zeroOffset);
                writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
            });
        });
    }

}
