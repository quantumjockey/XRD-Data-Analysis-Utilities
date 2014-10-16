package app.martiffviewport.components;

import DialogInitialization.FileSaveChooserWrapper;
import MvvmBase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import app.valueadjuster.ValueAdjuster;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import pathoperations.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.wrappers.filewrappers.TiffReader;
import xrdtiffoperations.wrappers.filewrappers.TiffWriter;
import xrdtiffvisualization.MARTiffVisualizer;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private ValueAdjuster maxBound;

    @FXML
    private ValueAdjuster minBound;

    @FXML
    private TitledPane viewportTitle;

    private MARTiffImage cachedImage;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportController(){
        maxBound = new ValueAdjuster();
        minBound = new ValueAdjuster();
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
        imageViewport.setImage(marImageGraph.RenderDataAsImage(null));
        cachedImage = image;

        int max = image.GetMaxValue();
        int min = image.GetMinValue();
        maxBound.getController().setLimiters(min, max);
        maxBound.getController().setDisplayedValue(max);
        minBound.getController().setLimiters(min, max);
        minBound.getController().setDisplayedValue(min);
    }

    public void RenderImageFromFile(PathWrapper filePath) throws IOException {
        cachedImage = ReadImageData(filePath);
        RenderImage(cachedImage);
    }

    public void setViewportTitle(String title){
        viewportTitle.setText(title);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks() {

    }

}
