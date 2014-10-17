package app.mainwindow;

import dialoginitialization.DirectoryChooserWrapper;
import mvvmbase.window.WindowControllerBase;
import app.martiffviewport.MARTiffViewport;
import xrdtiffoperations.math.DataSubtraction;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pathoperations.PathWrapper;
import pathoperations.filters.FilterWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import java.io.*;
import java.util.*;

public class Controller extends WindowControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private MARTiffViewport selectedImageViewport;

    @FXML
    private MARTiffViewport subtractedImageViewport;

    @FXML
    private MARTiffViewport resultantImageViewport;

    @FXML
    private TableView<PathWrapper> availableImages;

    @FXML
    private TableColumn<PathWrapper, String> selectedPath;

    @FXML
    private TableView<PathWrapper> subtractedImages;

    @FXML
    private TableColumn<PathWrapper, String> subtractedPath;

    private File selectedDirectory;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public Controller() {
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
    public void getDirectoryToDisplay() throws IOException{
        DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Select Directory for Images");
        selectedDirectory = dialog.getSelectedDirectory();
        if (selectedDirectory != null) {
            parseSelectedDirectory();
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void parseSelectedDirectory() throws IOException{
        FilterWrapper tiffFilter = new FilterWrapper(new String[]{".tif", ".tiff"});
        File[] images = selectedDirectory.listFiles(tiffFilter.filter);
        ArrayList<PathWrapper> imagesPaths = new ArrayList<>();
        for (File item : images){
            PathWrapper wrapper = new PathWrapper(item.getPath());
            imagesPaths.add(wrapper);
        }
        populateTableView(availableImages, selectedPath, imagesPaths, selectedImageViewport);
        populateTableView(subtractedImages, subtractedPath, imagesPaths, subtractedImageViewport);
        try{
            selectedImageViewport.renderImageFromFile(availableImages.getSelectionModel().selectedItemProperty().get());
            subtractedImageViewport.renderImageFromFile(subtractedImages.getSelectionModel().selectedItemProperty().get());
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
        subtractImages();
    }

    private void populateTableView(TableView<PathWrapper> tableControl, TableColumn<PathWrapper, String> columnControl, ArrayList<PathWrapper> paths, MARTiffViewport imageViewport){
        tableControl.setItems(FXCollections.observableList(paths));
        tableControl.getSelectionModel().select(0);
        prepareTableView(tableControl, columnControl, imageViewport);
    }

    private void prepareTableView(TableView<PathWrapper> tableContainer, TableColumn<PathWrapper, String> selectableColumn, MARTiffViewport imageViewport){
        selectableColumn.setCellValueFactory(new PropertyValueFactory<>("pathTail"));
        setTableViewChangeListeners(tableContainer, imageViewport);
    }

    private void setTableViewChangeListeners(TableView<PathWrapper> tableObject, MARTiffViewport imageViewport){
        tableObject.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableObject.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(Change<? extends Integer> change)
            {
                try {
                    imageViewport.renderImageFromFile(tableObject.getSelectionModel().selectedItemProperty().get());
                    subtractImages();
                }
                catch (IOException ex){
                    System.out.println("Image file could not be read!");
                }
            }
        });
    }

    private void subtractImages() throws IOException{
        MARTiffImage resultantImage = DataSubtraction.SubtractImages(selectedImageViewport.getController().getCachedImage(), subtractedImageViewport.getController().getCachedImage(), true);
        resultantImageViewport.renderImage(resultantImage);
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks(){
        selectedImageViewport.getController().setViewportTitle("Selected Image");
        subtractedImageViewport.getController().setViewportTitle("Subtracted Image");
        resultantImageViewport.getController().setViewportTitle("Resultant Image");
    }

}
