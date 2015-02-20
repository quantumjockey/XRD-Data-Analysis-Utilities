package app.controls.diffractionframeviewport.components;

import app.controls.dataexportcontrol.DataExportControl;
import app.filesystem.FileSysReader;
import app.filesystem.FileSysWriter;
import app.controls.zoomcontrol.ZoomControl;
import com.quantumjockey.dialogs.FileSaveChooserWrapper;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.quantumjockey.mvvmbase.action.ActionDelegate;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import app.controls.maskoptionscontrol.MaskOptionsControl;
import app.controls.renderoptionscontrol.RenderOptionsControl;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import com.quantumjockey.paths.PathWrapper;
import com.quantumjockey.paths.SystemAttributes;
import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.FileTypes;
import xrdtiffoperations.math.DataMasking;
import xrdtiffvisualization.DiffractionFrameVisualizer;
import com.quantumjockey.colorramps.GradientRamp;
import xrdtiffvisualization.masking.BoundedMask;

public class DiffractionFrameViewportController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double AUTO_ZOOM_INCREMENT = 0.5;
    private final double DEFAULT_ZOOM_MAX = 6.0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private TitledPane viewportTitle;

    @FXML
    private ScrollPane scrollViewport;

    @FXML
    private Label pixelTrack;

    @FXML
    private MaskOptionsControl maskOptions;

    @FXML
    private RenderOptionsControl renderOptions;

    @FXML
    private DataExportControl exportOptions;

    @FXML
    private ZoomControl imageZoom;

    private DiffractionFrame cachedImage;
    private GradientRamp selectedRamp;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        cachedImage = FileSysReader.readImageData(filePath);
        renderImageOnLoad(cachedImage);
    }

    public void renderImageOnLoad(DiffractionFrame image) throws IOException {

        int priorLowerBnd = maskOptions.getController().getLowerBound();
        int priorUpperBnd = maskOptions.getController().getUpperBound();

        updateMaskLimiters(image);

        if (cachedImage != null) {
            int priorMax, priorMin;

            priorMax = cachedImage.getMaxValue();
            priorMin = cachedImage.getMinValue();

            if (maskOptions.getController().getStickyBounds()) {
                if (priorLowerBnd > cachedImage.getMinValue() && priorLowerBnd != priorMin)
                    maskOptions.getController().setLowerBound(priorLowerBnd);
                if (priorUpperBnd < cachedImage.getMaxValue() && priorUpperBnd != priorMax)
                    maskOptions.getController().setUpperBound(priorUpperBnd);
            }
        }

        cachedImage = image;
        updatePixelScale(image);
        updateZoomScale(image);
        renderImageWithMask(image, renderOptions.getController().getAdaptiveRender());
        viewportTitle.setText(image.getIdentifier());
    }

    public void renderImageWithMask(DiffractionFrame image, boolean isAdaptive) throws IOException {
        DiffractionFrameVisualizer marImageGraph = new DiffractionFrameVisualizer(image);
        imageViewport.setSmooth(false);
        imageViewport.setImage(marImageGraph.renderDataAsImage(
                selectedRamp,
                new BoundedMask(
                        maskOptions.getController().getLowerBound(),
                        maskOptions.getController().getUpperBound(),
                        maskOptions.getController().getMaskHue()),
                isAdaptive
        ));
        cachedImage = image;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void exportImage(String imageType){

        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        int maskLb = maskOptions.getController().getLowerBound();
        int maskUb = maskOptions.getController().getUpperBound();
        boolean isMasked = (cachedImage.getMaxValue() != maskUb || cachedImage.getMinValue() != maskLb);

        if(isMasked){
            DataMasking.maskImage(cachedImage, maskLb, maskUb);
            dialog.setInitialFileName(cachedImage.getIdentifier());
        }
        else {
            dialog.setInitialFileName(cachedImage.getIdentifier());
        }

        File destination = dialog.getSaveDirectory();

        FileSysWriter.writeImageData(destination, cachedImage, imageType);
    }

    private Void exportThirtyTwoBitIntImage(){
        exportImage(FileTypes.TIFF_32_BIT_INT);
        return null;
    }

    private Void exportThirtyTwoBitFloatImage(){
        exportImage(FileTypes.TIFF_32_BIT_FLOAT);
        return null;
    }

    private void updateMaskLimiters(DiffractionFrame image){
        int max = image.getMaxValue();
        int min = image.getMinValue();
        maskOptions.getController().setLimiters(min, max);
    }

    private void updatePixelScale(DiffractionFrame image){
        if (image == null){
            renderOptions.getController().setOffset(0);
        }
        else {
            renderOptions.getController().setOffset(Math.abs(image.getMinValue()));
        }
    }

    private void updateZoomScale(DiffractionFrame image){
        if (image != null) {
            int size = (image.getHeight() >= image.getWidth()) ? image.getHeight() : image.getWidth();
            double viewportSize = scrollViewport.getWidth() - 2;
            double scale = viewportSize / (double) size;
            imageZoom.getController().setZoomBounds(scale, DEFAULT_ZOOM_MAX);
            imageZoom.getController().setZoomLevel(scale);
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        exportOptions = new DataExportControl();
        maskOptions = new MaskOptionsControl();
        renderOptions = new RenderOptionsControl();
        imageZoom = new ZoomControl();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults() {
        updatePixelScale(cachedImage);
        ArrayList<ActionDelegate<Void>> exportActions;
        exportActions = new ArrayList<>();
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_INT, this::exportThirtyTwoBitIntImage));
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_FLOAT, this::exportThirtyTwoBitFloatImage));
        exportOptions.getController().updateSelections(exportActions);
        selectedRamp = renderOptions.getController().getActiveRamp();
        viewportTitle.setText("(No Data Selected)");
    }

    @Override
    protected void setListeners(){

        EventHandler<MouseEvent> clickEvent = (event) -> {
            if (event.getClickCount() == 2) {
                double currentZoom = imageZoom.getController().getZoomLevel();
                double max = imageZoom.getController().getMaxZoom();
                double min = imageZoom.getController().getMinZoom();
                if (event.getButton() == MouseButton.SECONDARY) {
                    double newValue = currentZoom - AUTO_ZOOM_INCREMENT;
                    imageZoom.getController().setZoomLevel((newValue < min) ? min : newValue);
                } else {
                    double newValue = currentZoom + AUTO_ZOOM_INCREMENT;
                    imageZoom.getController().setZoomLevel((newValue > max) ? max : newValue);
                }
            }
        };

        EventHandler<MouseEvent> exitEvent = (event) -> pixelTrack.setText("");

        EventHandler<MouseEvent> movedEvent = (event) -> {
            double imageX = cachedImage.getWidth();
            double realX = event.getX();
            double viewportX = imageViewport.getFitWidth();
            int scaledX = (int)((realX / viewportX) * imageX);

            double imageY = cachedImage.getHeight();
            double realY = event.getY();
            double viewportY = imageViewport.getFitHeight();
            int scaledY = (int)((realY / viewportY) * imageY);

            // (imageY - scaledY) used for display to represent 0,0 in bottom-left corner of image
            String message = ((imageZoom.getController().getZoomLevel() < 1) ? "[Approximate] " : "")
                    + "Coordinates (x,y): " + scaledX + "," + ((int)imageY - scaledY)
                    + " - Intensity: " + cachedImage.getIntensityMapValue(scaledY, scaledX);

            pixelTrack.setFont(Font.font(null, FontWeight.BOLD, 13));
            pixelTrack.setText(message);

            String tooltip = ((imageZoom.getController().getZoomLevel() < 1) ? "[Approx.] " : "")
                    + "(x,y): " + scaledX + "," + ((int)imageY - scaledY) + SystemAttributes.LINE_SEPARATOR
                    + "Intensity: " + cachedImage.getIntensityMapValue(scaledY, scaledX);

            scrollViewport.setTooltip(new Tooltip(tooltip));
        };

        ChangeListener<Boolean> onAdaptiveRenderingChange = (observable, oldValue, newValue) -> {
            try {
                if(newValue)
                    maskOptions.getController().setMaskHue(renderOptions.getController().getActiveRamp().getRampColorValue(0.0));
                renderImageWithMask(cachedImage, newValue);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Color> onHueChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<GradientRamp> onRampChange = (observable, oldValue, newValue) -> {
            try {
                selectedRamp = newValue;
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Number> onScaleChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Boolean> onStickyBoundsChange = (observable, oldValue, newValue) -> {
            try {
                renderImageOnLoad(cachedImage);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Number> onZoomChange = (observable, oldValue, newValue) -> {
            if (cachedImage != null){
                double vVal = scrollViewport.getVvalue();
                double hVal = scrollViewport.getHvalue();
                int height = cachedImage.getHeight();
                double heightScaled = newValue.doubleValue() * height;
                int width = cachedImage.getWidth();
                double widthScaled = newValue.doubleValue() * width;
                imageViewport.setFitHeight(heightScaled);
                imageViewport.setFitWidth(widthScaled);
                scrollViewport.setVvalue(vVal);
                scrollViewport.setHvalue(hVal);
            }
        };

        renderOptions.getController().activeRampProperty().addListener(onRampChange);
        renderOptions.getController().adaptiveRenderProperty().addListener(onAdaptiveRenderingChange);
        maskOptions.getController().lowerBoundProperty().addListener(onScaleChange);
        maskOptions.getController().upperBoundProperty().addListener(onScaleChange);
        maskOptions.getController().maskHueProperty().addListener(onHueChange);
        maskOptions.getController().stickyBoundsProperty().addListener(onStickyBoundsChange);
        imageZoom.getController().zoomLevelProperty().addListener(onZoomChange);
        imageViewport.setOnMouseClicked(clickEvent);
        imageViewport.setOnMouseExited(exitEvent);
        imageViewport.setOnMouseMoved(movedEvent);
    }

}
