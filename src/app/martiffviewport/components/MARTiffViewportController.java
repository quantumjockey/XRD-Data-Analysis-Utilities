package app.martiffviewport.components;

import app.dataexportcontrol.DataExportControl;
import app.zoomcontrol.ZoomControl;
import dialoginitialization.FileSaveChooserWrapper;
import javafx.scene.control.ScrollPane;
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
import pathoperations.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataMasking;
import xrdtiffoperations.wrappers.filewrappers.TiffReader;
import xrdtiffoperations.wrappers.filewrappers.TiffWriter;
import xrdtiffvisualization.MARTiffVisualizer;
import xrdtiffvisualization.colorramps.GradientRamp;
import xrdtiffvisualization.masking.BoundedMask;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double DEFAULT_ZOOM_MAX = 6.0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private TitledPane viewportTitle;

    @FXML
    private ScrollPane scrollViewport;

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
        cachedImage = readImageData(filePath);
        renderImage(cachedImage);
    }

    public void renderImageWithMask(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.renderDataAsImage(selectedRamp, new BoundedMask(maskOptions.getController().getLowerBound(), maskOptions.getController().getUpperBound(), maskOptions.getController().getMaskHue())));
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
            dialog.setInitialFileName(masked.filename);
        }
        else {
            dialog.setInitialFileName(cachedImage.filename);
        }
        File destination = dialog.getSaveDirectory();
        writeImageDataToFile(destination, (withMask) ? masked : cachedImage);
    }

    private Void exportMaskedImage(){
        exportImage(true);
        return null;
    }

    private Void exportRawImage(){
        exportImage(false);
        return null;
    }

    private MARTiffImage readImageData(PathWrapper imagePath) throws IOException {
        MARTiffImage temp = null;
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(imagePath.getInjectedPath());
            marImageReader.readFileData(false);
            temp = marImageReader.getImageData();
        }
        return temp;
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

    private void writeImageDataToFile(File path, MARTiffImage image){
        if (path != null) {
            TiffWriter writer = new TiffWriter(image);
            writer.write(path.getPath());
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
    }

}
