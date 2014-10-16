package app.martiffviewport.components;

import DialogInitialization.FileSaveChooserWrapper;
import MvvmBase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import app.valueadjuster.ValueAdjuster;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
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
    private ChoiceBox<String> availableRamps;

    @FXML
    private ImageView imageViewport;

    @FXML
    private ValueAdjuster maxBound;

    @FXML
    private ValueAdjuster minBound;

    @FXML
    private TitledPane viewportTitle;

    @FXML
    private ColorPicker overlayHue;

    private MARTiffImage cachedImage;
    private ArrayList<GradientRamp> ramps;
    private GradientRamp selectedRamp;
    private BoundedMask mask;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportController(){
        CreateCustomControlInstances();
        CreateRamps();
        CreateMasks();
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
        imageViewport.setImage(marImageGraph.RenderDataAsImage(selectedRamp, mask));
        cachedImage = image;
    }

    public void setViewportTitle(String title){
        viewportTitle.setText(title);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void CreateCustomControlInstances() {
        maxBound = new ValueAdjuster();
        minBound = new ValueAdjuster();
    }

    private void CreateMasks() {
        mask = new BoundedMask(minBound.getController().getMinValue(), maxBound.getController().getMaxValue(), Color.WHITE);
    }

    private void CreateRamps() {
        ramps = new ArrayList<>();
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}, "Spectrum Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.RED, Color.ORANGE, Color.YELLOW}, "Autumn Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.YELLOW, Color.WHITE}, "High Contrast Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.WHITE}, "Grayscale Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.WHITE, Color.BLACK}, "Inverse Grayscale Ramp"));
        selectedRamp = ramps.get(0);
    }

    private void InitializeListeners(){
        maxBound.getController().displayedValueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mask.upperBound = newValue.intValue();
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        minBound.getController().displayedValueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mask.lowerBound = newValue.intValue();
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
        overlayHue.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if (newValue != null){
                    mask.maskHue = newValue;
                }
                else {
                    mask.maskHue = oldValue;
                }
                try {
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
    }

    private void InitializeRamps() {
        ArrayList<String> rampList = new ArrayList<>();
        for (GradientRamp item : ramps){
            rampList.add(item.tag);
        }
        availableRamps.getItems().clear();
        availableRamps.setItems(FXCollections.observableList(rampList));
        availableRamps.getSelectionModel().select(0);
        availableRamps.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number new_value) {
                try {
                    selectedRamp = ramps.get(new_value.intValue());
                    RenderImageWithMask(cachedImage);
                } catch (IOException ex) {
                    System.out.println("Image render error!");
                }
            }
        });
    }

    @Override
    protected void performInitializationTasks() {
        InitializeRamps();
        InitializeListeners();
    }

    private void updateMaskLimiters(MARTiffImage image){
        int max = image.GetOffsetMaxValue();
        int min = image.GetOffsetMinValue();
        minBound.getController().setLimiters(min, max);
        minBound.getController().setDisplayedValue(min);
        maxBound.getController().setLimiters(min, max);
        maxBound.getController().setDisplayedValue(max);
    }

}
