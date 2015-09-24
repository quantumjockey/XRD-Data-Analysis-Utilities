package edu.hipsec.xrddau.xrdvisualization.engine;

import com.quantumjockey.colorramps.GradientRamp;
import com.quantumjockey.melya.concurrency.BackgroundTask;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrddau.xrdvisualization.engine.base.DataMappingEngine;
import edu.hipsec.xrddau.xrdvisualization.masking.BoundedMask;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.io.IOException;

public class FalseColorMappingEngine extends DataMappingEngine {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Image renderData(DiffractionFrame data, GradientRamp ramp, BoundedMask mask, boolean adaptive) {
        WritableImage displayed = new WritableImage(data.getWidth(), data.getHeight());
        BackgroundTask.execute(() -> {
            try {
                if (mask != null)
                    this.renderImageWithMask(data, displayed.getPixelWriter(), data.getMaxValue(), ramp, mask, adaptive);
                else
                    this.renderImageViaColorRamp(data, displayed.getPixelWriter(), data.getMaxValue(), ramp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return displayed;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void renderImageViaColorRamp(DiffractionFrame data, PixelWriter writer, int maxValue, GradientRamp ramp) throws IOException {
        GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(this.DEFAULT_RAMP)) : ramp;
        final int zeroOffset = data.scaleImageZero();
        data.cycleFramePixels((y, x) -> {
            int value = data.getIntensityMapValue(y, x);
            double coefficient = (double) (value + zeroOffset) / (double) (maxValue + zeroOffset);
            writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
        });
    }

    private void renderImageWithMask(DiffractionFrame data, PixelWriter writer, int maxValue, GradientRamp ramp, BoundedMask mask, boolean adaptive) throws IOException {
        GradientRamp colorRamp = (ramp == null) ? (new GradientRamp(this.DEFAULT_RAMP)) : ramp;
        final int zeroOffset = (adaptive) ? mask.getLowerBound() : data.scaleImageZero();
        data.cycleFramePixels((y, x) -> {
            int value = data.getIntensityMapValue(y, x);
            if (value < mask.getLowerBound() || value > mask.getUpperBound())
                writer.setColor(x, y, mask.getMaskHue());
            else {
                double coefficient = (double) (value + zeroOffset) / (double) (maxValue + zeroOffset);
                writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
            }
        });
    }

}
