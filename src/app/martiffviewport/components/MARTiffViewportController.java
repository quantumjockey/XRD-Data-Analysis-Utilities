package app.martiffviewport.components;

import dialoginitialization.FileSaveChooserWrapper;
import mvvmbase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import app.maskoptionscontrol.MaskOptionsControl;
import app.renderoptionscontrol.RenderOptionsControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import pathoperations.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.wrappers.filewrappers.TiffReader;
import xrdtiffoperations.wrappers.filewrappers.TiffWriter;
import xrdtiffvisualization.MARTiffVisualizer;
import xrdtiffvisualization.colorramps.GradientRamp;
import xrdtiffvisualization.masking.BoundedMask;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private TitledPane viewportTitle;

    @FXML
    private MaskOptionsControl maskOptions;

    @FXML
    private RenderOptionsControl renderOptions;

    private MARTiffImage cachedImage;
    private GradientRamp selectedRamp;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportController(){
        createCustomControlInstances();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void exportImage(){
        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        dialog.setInitialFileName(cachedImage.filename);
        File destination = dialog.getSaveDirectory();
        if (destination != null) {
            TiffWriter writer = new TiffWriter(cachedImage);
            writer.write(destination.getPath());
        }
    }

    public MARTiffImage getCachedImage() {
        return cachedImage;
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

    public void renderImage(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.renderDataAsImage(selectedRamp));
        cachedImage = image;
        updateMaskLimiters(image);
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

    private void createCustomControlInstances() {
        maskOptions = new MaskOptionsControl();
        renderOptions = new RenderOptionsControl();
    }

    private void initializeListeners(){
        renderOptions.getController().activeRampProperty().addListener(new ChangeListener<GradientRamp>() {
            @Override
            public void changed(ObservableValue<? extends GradientRamp> observable, GradientRamp oldValue, GradientRamp newValue) {
                try {
                    selectedRamp = newValue;
                    renderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().lowerBoundProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    renderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().upperBoundProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    renderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().maskHueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                try {
                    renderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
    }

    @Override
    protected void performInitializationTasks() {
        initializeListeners();
    }

    private void updateMaskLimiters(MARTiffImage image){
        int max = image.getOffsetMaxValue();
        int min = image.getOffsetMinValue();
        maskOptions.getController().setLimiters(min, max);
    }

}
