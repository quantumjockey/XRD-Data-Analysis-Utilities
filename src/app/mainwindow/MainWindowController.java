package app.mainwindow;

import dialoginitialization.DirectoryChooserWrapper;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import mvvmbase.window.WindowControllerBase;
import app.martiffviewport.MARTiffViewport;
import xrdtiffoperations.math.DataSubtraction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import pathoperations.PathWrapper;
import pathoperations.filters.FilterWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
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

    @FXML
    private ComboBox<String> selectedPath;

    @FXML
    private ComboBox<String> subtractedPath;

    @FXML
    private Label rootPath;

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
            rootPath.setText(selectedDirectory.getPath());
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
        ChangeListener<String> selectedChanged = createListener(selectedPath, selectedImageViewport);
        populateComboBox(selectedPath, temp, selectedChanged);
        ChangeListener<String> subtractedChanged = createListener(subtractedPath, subtractedImageViewport);
        populateComboBox(subtractedPath, temp, subtractedChanged);
    }

    private void populateComboBox(ComboBox<String> selector, ArrayList<String> temp, ChangeListener<String> onSelectionChanged){
        selector.getItems().clear();
        selector.setItems(FXCollections.observableList(temp));
        selector.getSelectionModel().select(0);
        selector.setEditable(false);
        selector.valueProperty().addListener(onSelectionChanged);
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
        selectedImageViewport.getController().setViewportTitle("Selected Image");
        subtractedImageViewport.getController().setViewportTitle("Subtracted Image");
        resultantImageViewport.getController().setViewportTitle("Resultant Image");
        setDefaultToolAssortment();
        rootPath.setText("(Unspecified)");
    }

}
