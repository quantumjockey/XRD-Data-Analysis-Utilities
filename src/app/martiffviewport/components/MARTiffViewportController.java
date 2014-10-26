package app.martiffviewport.components;

import app.dataexportcontrol.DataExportControl;
import app.filesystem.FileSysReader;
import app.filesystem.FileSysWriter;
import app.zoomcontrol.ZoomControl;
import filesystembase.dialogwrappers.FileSaveChooserWrapper;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mvvmbase.action.ActionDelegate;
import mvvmbase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import app.maskoptionscontrol.MaskOptionsControl;
import app.renderoptionscontrol.RenderOptionsControl;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import filesystembase.paths.PathWrapper;
import filesystembase.paths.SystemAttributes;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataMasking;
import xrdtiffoperations.visualization.MARTiffVisualizer;
import colorramps.GradientRamp;
import xrdtiffoperations.visualization.masking.BoundedMask;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double DEFAULT_ZOOM_MAX = 6.0;
    private final double AUTO_ZOOM_INCREMENT = 0.5;

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

    private MARTiffImage cachedImage;
    private GradientRamp selectedRamp;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MARTiffImage getCachedImage() {
        return cachedImage;
    }

    public void renderImage(MARTiffImage image) throws IOException {
        cachedImage = image;
        updateMaskLimiters(image);
        updatePixelScale(image);
        updateZoomScale(image);
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.renderDataAsImage(selectedRamp));
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        cachedImage = FileSysReader.readImageData(filePath);
        renderImage(cachedImage);
    }

    public void renderImageWithMask(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.renderDataAsImage(
                selectedRamp,
                new BoundedMask(
                        maskOptions.getController().getLowerBound(),
                        maskOptions.getController().getUpperBound(),
                        maskOptions.getController().getMaskHue())));
        cachedImage = image;
    }

    public void setViewportTitle(String title){
        viewportTitle.setText(title);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void exportImage(boolean withMask){
        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        MARTiffImage masked = null;
        int maskLb = maskOptions.getController().getLowerBound();
        int maskUb = maskOptions.getController().getUpperBound();
        if(withMask){
            masked = DataMasking.maskImage(cachedImage, maskLb, maskUb);
            dialog.setInitialFileName(masked.getFilename());
        }
        else {
            dialog.setInitialFileName(cachedImage.getFilename());
        }
        File destination = dialog.getSaveDirectory();
        FileSysWriter.writeImageData(destination, (withMask) ? masked : cachedImage);
    }

    private Void exportMaskedImage(){
        exportImage(true);
        return null;
    }

    private Void exportRawImage(){
        exportImage(false);
        return null;
    }

    private void updateMaskLimiters(MARTiffImage image){
        int max = image.getMaxValue();
        int min = image.getMinValue();
        maskOptions.getController().setLimiters(min, max);
    }

    private void updatePixelScale(MARTiffImage image){
        if (image == null){
            renderOptions.getController().setOffset(0);
        }
        else {
            renderOptions.getController().setOffset(Math.abs(image.getMinValue()));
        }
    }

    private void updateZoomScale(MARTiffImage image){
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
        exportActions.add(new ActionDelegate<>("Raw Data", this::exportRawImage));
        exportActions.add(new ActionDelegate<>("Masked Data", this::exportMaskedImage));
        exportOptions.getController().updateSelections(exportActions);
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

            String message = ((imageZoom.getController().getZoomLevel() < 1) ? "[Approximate] " : "")
                    + "Coordinates (x,y): " + scaledX + "," + scaledY
                    + " - Intensity: " + cachedImage.getIntensityMapValue(scaledY, scaledX);

            pixelTrack.setFont(Font.font(null, FontWeight.BOLD, 13));
            pixelTrack.setText(message);

            String tooltip = ((imageZoom.getController().getZoomLevel() < 1) ? "[Approx.] " : "")
                    + "(x,y): " + scaledX + "," + scaledY + SystemAttributes.LINE_SEPARATOR
                    + "Intensity: " + cachedImage.getIntensityMapValue(scaledY, scaledX);

            scrollViewport.setTooltip(new Tooltip(tooltip));
        };

        ChangeListener<Color> onHueChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<GradientRamp> onRampChange = (observable, oldValue, newValue) -> {
            try {
                selectedRamp = newValue;
                renderImageWithMask(cachedImage);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Number> onScaleChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage);
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
        maskOptions.getController().lowerBoundProperty().addListener(onScaleChange);
        maskOptions.getController().upperBoundProperty().addListener(onScaleChange);
        maskOptions.getController().maskHueProperty().addListener(onHueChange);
        imageZoom.getController().zoomLevelProperty().addListener(onZoomChange);
        imageViewport.setOnMouseClicked(clickEvent);
        imageViewport.setOnMouseExited(exitEvent);
        imageViewport.setOnMouseMoved(movedEvent);
    }

}
