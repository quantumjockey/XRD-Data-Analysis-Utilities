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

    public int height;
    public int width;
    private MARTiffImage data;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public MARTiffVisualizer(MARTiffImage imageData){
        height = imageData.ifdListing.get(0).GetTagValue((short)257);
        width = imageData.ifdListing.get(0).GetTagValue((short)256);
        data = imageData;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public Image RenderImageInGrayScale() throws IOException{
        WritableImage displayed = new WritableImage(data.intensityMap[0].length, data.intensityMap.length);
        PixelWriter writer = displayed.getPixelWriter();
        short maxValue = data.GetMaxValue();
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
        return displayed;
    }

    public Image RenderImageViaColorRamp() throws IOException{
        WritableImage displayed = new WritableImage(data.intensityMap[0].length, data.intensityMap.length);
        PixelWriter writer = displayed.getPixelWriter();
        short maxValue = data.GetMaxValue();

        Color[] ramp_colors = { Color.BLACK, Color.BLUE, Color.GREEN};
        GradientRamp colorRamp = new GradientRamp(ramp_colors);

        for (int x = 0; x < data.intensityMap[0].length; x++) {
            for (int y = 0; y < data.intensityMap.length; y++) {
                short value = data.intensityMap[y][x];
                if (value < 0) {
                    writer.setColor(x, y, Color.RED);
                }
                else {
                    int coefficient = value / maxValue;
                    writer.setColor(x, y, colorRamp.getRampColorValue(coefficient));
                }
            }
        }
        return displayed;
    }

}
