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
        CreateCustomControlInstances();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void ExportImage(){
        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        dialog.SetInitialFileName(cachedImage.filename);
        File destination = dialog.GetSaveDirectory();
        if (destination != null) {
            TiffWriter writer = new TiffWriter(cachedImage);
            writer.Write(destination.getPath());
        }
    }

    public MARTiffImage getCachedImage() {
        return cachedImage;
    }

    private MARTiffImage ReadImageData(PathWrapper imagePath) throws IOException {
        MARTiffImage temp = null;
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(imagePath.getInjectedPath());
            marImageReader.ReadFileData(false);
            temp = marImageReader.GetImageData();
        }
        return temp;
    }

    public void RenderImage(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.RenderDataAsImage(selectedRamp));
        cachedImage = image;
        updateMaskLimiters(image);
    }

    public void RenderImageFromFile(PathWrapper filePath) throws IOException {
        cachedImage = ReadImageData(filePath);
        RenderImage(cachedImage);
    }

    public void RenderImageWithMask(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.RenderDataAsImage(selectedRamp, new BoundedMask(maskOptions.getController().getLowerBound(), maskOptions.getController().getUpperBound(), maskOptions.getController().getMaskHue())));
        cachedImage = image;
    }

    public void setViewportTitle(String title){
        viewportTitle.setText(title);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void CreateCustomControlInstances() {
        maskOptions = new MaskOptionsControl();
        renderOptions = new RenderOptionsControl();
    }

    private void InitializeListeners(){
        renderOptions.getController().activeRampProperty().addListener(new ChangeListener<GradientRamp>() {
            @Override
            public void changed(ObservableValue<? extends GradientRamp> observable, GradientRamp oldValue, GradientRamp newValue) {
                try {
                    selectedRamp = newValue;
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().lowerBoundProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().upperBoundProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        maskOptions.getController().maskHueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
    }

    @Override
    protected void performInitializationTasks() {
        InitializeListeners();
    }

    private void updateMaskLimiters(MARTiffImage image){
        int max = image.GetOffsetMaxValue();
        int min = image.GetOffsetMinValue();
        maskOptions.getController().setLimiters(min, max);
    }

}
