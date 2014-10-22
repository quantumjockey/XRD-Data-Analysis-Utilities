package app.mainwindow;

import dialoginitialization.DirectoryChooserWrapper;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import mvvmbase.controls.ComboBoxExt;
import mvvmbase.controls.LabelExt;
import mvvmbase.controls.ListViewExt;
import mvvmbase.window.WindowControllerBase;
import app.martiffviewport.MARTiffViewport;
import pathoperations.SystemAttributes;
import xrdtiffoperations.math.DataSubtraction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import pathoperations.PathWrapper;
import pathoperations.filters.FilterWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.wrappers.filewrappers.TiffReader;
import xrdtiffoperations.wrappers.filewrappers.TiffWriter;

import java.io.*;
import java.util.*;

public class MainWindowController extends WindowControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private MARTiffViewport selectedImageViewport;

    @FXML
    private MARTiffViewport subtractedImageViewport;

    @FXML
    private MARTiffViewport resultantImageViewport;

    // Single-Image Subtraction

    @FXML
    private ComboBox<String> selectedPath;

    @FXML
    private ComboBox<String> subtractedPath;

    @FXML
    private Label rootPath;

    // Multiple-Image Subtraction

    @FXML
    private ListView<String> selectedPathMulti;

    @FXML
    private ComboBox<String> subtractedPathMulti;

    @FXML
    private Label rootPathMulti;

    // Tools Area

    @FXML
    private Accordion toolsContainer;

    private ArrayList<PathWrapper> availableFiles;
    private File selectedDirectory;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public MainWindowController() {
        super();
        selectedImageViewport = new MARTiffViewport();
        subtractedImageViewport = new MARTiffViewport();
        resultantImageViewport = new MARTiffViewport();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void exitApplication(){
        System.exit(0);
    }

    @FXML
    public void getDirectoryToDisplay(){
        DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Select Directory for Images");
        selectedDirectory = dialog.getSelectedDirectory();
        if (selectedDirectory != null) {
            availableFiles = parseSelectedDirectory();
            populateControls();
            String path = selectedDirectory.getPath();
            LabelExt.update(rootPath, path, path);
            LabelExt.update(rootPathMulti, path, path);
            try{
                selectedImageViewport.renderImageFromFile(availableFiles.get(selectedPath.getSelectionModel().getSelectedIndex()));
                subtractedImageViewport.renderImageFromFile(availableFiles.get(subtractedPath.getSelectionModel().getSelectedIndex()));
                subtractImages();
            }
            catch (IOException ex){
                System.out.println("Image file could not be rendered!");
            }
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<String> createListener(ComboBox<String> selector, MARTiffViewport imageViewport) {
        return (observable, oldValue, newValue) -> {
            try {
                selector.setTooltip(new Tooltip(selector.getSelectionModel().getSelectedItem()));
                imageViewport.renderImageFromFile(availableFiles.get(selector.getSelectionModel().getSelectedIndex()));
                subtractImages();
            }
            catch (IOException ex){
                System.out.println("Image file could not be read!");
            }
        };
    }

    private ArrayList<PathWrapper> parseSelectedDirectory(){
        FilterWrapper tiffFilter = new FilterWrapper(new String[]{".tif", ".tiff"});
        File[] images = selectedDirectory.listFiles(tiffFilter.filter);
        ArrayList<PathWrapper> imagesPaths = new ArrayList<>();
        for (File item : images){
            PathWrapper wrapper = new PathWrapper(item.getPath());
            imagesPaths.add(wrapper);
        }
        return imagesPaths;
    }

    private void populateControls(){
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        // Single Image Subtraction
        ChangeListener<String> selectedChanged = createListener(selectedPath, selectedImageViewport);
        ComboBoxExt.populate(selectedPath, temp, selectedChanged);
        ChangeListener<String> subtractedChanged = createListener(subtractedPath, subtractedImageViewport);
        ComboBoxExt.populate(subtractedPath, temp, subtractedChanged);

        // Multiple Image Subtraction
        ListViewExt.populate(selectedPathMulti, temp, null, SelectionMode.MULTIPLE, false);
        ComboBoxExt.populate(subtractedPathMulti, temp, null);
    }

    private void setDefaultToolAssortment(){
        toolsContainer.setExpandedPane(toolsContainer.getPanes().get(0));
    }

    private void subtractImages() throws IOException{
        MARTiffImage resultantImage = DataSubtraction.subtractImages(selectedImageViewport.getController().getCachedImage(), subtractedImageViewport.getController().getCachedImage());
        resultantImageViewport.renderImage(resultantImage);
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks(){
        String rootDefault = "(Unspecified)";
        selectedImageViewport.getController().setViewportTitle("Selected Image");
        subtractedImageViewport.getController().setViewportTitle("Subtracted Image");
        resultantImageViewport.getController().setViewportTitle("Resultant Image");
        setDefaultToolAssortment();
        LabelExt.update(rootPath, rootDefault, null);
        LabelExt.update(rootPathMulti, rootDefault, null);
    }


    ////// Multiple-Image Subtraction //////

    @FXML
    public void subtractImageGroup() throws IOException{

        // Get destination directory
        DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Save to...");
        File destination = dialog.getSelectedDirectory();

        if (destination != null) {

            // Get Items from ListView
            ArrayList<PathWrapper> selected = new ArrayList<>();
            selectedPathMulti.getSelectionModel().getSelectedIndices().forEach((index) -> selected.add(availableFiles.get(index)));

            // Get Subtracted image from ComboBox
            PathWrapper subtracted = availableFiles.get(subtractedPathMulti.getSelectionModel().getSelectedIndex());
            MARTiffImage subtractedImage = readImageData(subtracted);

            // Subtract Images & write data to file
            selected.forEach((path) -> {
                MARTiffImage firstImage = null;
                try{
                    firstImage = readImageData(path);
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
                if (firstImage != null && subtractedImage != null) {
                    MARTiffImage result = DataSubtraction.subtractImages(firstImage, subtractedImage);
                    String filePath = destination.getPath() + SystemAttributes.FILE_SEPARATOR + result.filename;
                    writeImageDataToFile(new File(filePath), result);
                }
            });

        }

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

    private void writeImageDataToFile(File path, MARTiffImage image){
        if (path != null) {
            TiffWriter writer = new TiffWriter(image);
            writer.write(path.getPath());
        }
    }

}
