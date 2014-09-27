package xrdtiffvisualization;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffvisualization.colorramps.GradientRamp;

import java.io.IOException;

/**
 * Created by quantumjockey on 9/22/14.
 */
public class MARTiffVisualizer {

    private MARTiffImage data;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public MARTiffVisualizer(MARTiffImage imageData){
        data = imageData;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public Image RenderDataAsImage(boolean grayScale) throws IOException {
        WritableImage displayed = new WritableImage(data.intensityMap[0].length, data.intensityMap.length);
        if(grayScale){
            RenderImageInGrayScale(displayed.getPixelWriter(), data.GetMaxValue());
        }
        else{
            RenderImageViaColorRamp(displayed.getPixelWriter(), data.GetMaxValue());
        }
        return displayed;
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void RenderImageInGrayScale(PixelWriter writer, short maxValue) throws IOException{
        for (int x = 0; x < data.intensityMap[0].length; x++) {
            for (int y = 0; y < data.intensityMap.length; y++) {
                short value = data.intensityMap[y][x];
                if (value < 0) {
                    writer.setColor(x, y, Color.RED);
                }
                else {
                    int byteVal = (255 * value) / maxValue;
                    writer.setColor(x, y, Color.rgb(byteVal, byteVal, byteVal));
                }
            }
        }
    }

    private void RenderImageViaColorRamp(PixelWriter writer, short maxValue) throws IOException{
        Color[] ramp_colors = { Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE};
        GradientRamp colorRamp = new GradientRamp(ramp_colors);
        for (int x = 0; x < data.intensityMap[0].length; x++) {
            for (int y = 0; y < data.intensityMap.length; y++) {
                short value = data.intensityMap[y][x];
                if (value < 0) {
                    writer.setColor(x, y, Color.RED);
                }
                else {
                    double coefficient = (double)value / (double)maxValue;
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient, 0.0, 1.0));
                }
            }
        }
    }

}
