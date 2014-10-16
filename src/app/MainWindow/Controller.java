package app.MainWindow;

import DialogInitialization.DirectoryChooserWrapper;
import MvvmBase.window.WindowControllerBase;
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

    // Main Viewer
    @FXML private MARTiffViewport selectedImageViewport;

    // Subtracted Viewer
    @FXML private MARTiffViewport subtractedImageViewport;

    // Result Viewer
    @FXML private MARTiffViewport resultantImageViewport;

    // File Listing
    @FXML private TableView<PathWrapper> availableImages;
    @FXML private TableColumn<PathWrapper, String> selectedPath;

    // Subtracted Listing
    @FXML private TableView<PathWrapper> subtractedImages;
    @FXML private TableColumn<PathWrapper, String> subtractedPath;

    // Fields
    private File selectedDirectory;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public Controller() {
        super();
        selectedImageViewport = new MARTiffViewport();
        subtractedImageViewport = new MARTiffViewport();
        resultantImageViewport = new MARTiffViewport();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void ExitApplication(){
        System.exit(0);
    }

    @FXML
    public void GetDirectoryToDisplay() throws IOException{
        DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Select Directory for Images");
        selectedDirectory = dialog.GetSelectedDirectory();
        if (selectedDirectory != null) {
            ParseSelectedDirectory();
        }
    }

    @Override
    protected void performInitializationTasks(){
        selectedImageViewport.getController().setViewportTitle("Selected Image");
        subtractedImageViewport.getController().setViewportTitle("Subtracted Image");
        resultantImageViewport.getController().setViewportTitle("Resultant Image");
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void ParseSelectedDirectory() throws IOException{
        FilterWrapper tiffFilter = new FilterWrapper(new String[]{".tif", ".tiff"});
        File[] images = selectedDirectory.listFiles(tiffFilter.filter);
        ArrayList<PathWrapper> imagesPaths = new ArrayList<>();
        for (File item : images){
            PathWrapper wrapper = new PathWrapper(item.getPath());
            imagesPaths.add(wrapper);
        }
        PopulateTableView(availableImages, selectedPath, imagesPaths, selectedImageViewport);
        PopulateTableView(subtractedImages, subtractedPath, imagesPaths, subtractedImageViewport);
        try{
            selectedImageViewport.RenderImageFromFile(availableImages.getSelectionModel().selectedItemProperty().get());
            subtractedImageViewport.RenderImageFromFile(subtractedImages.getSelectionModel().selectedItemProperty().get());
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
        SubtractImages();
    }

    private void PopulateTableView(TableView<PathWrapper> tableControl, TableColumn<PathWrapper, String> columnControl, ArrayList<PathWrapper> paths, MARTiffViewport imageViewport){
        tableControl.setItems(FXCollections.observableList(paths));
        tableControl.getSelectionModel().select(0);
        PrepareTableView(tableControl, columnControl, imageViewport);
    }

    private void PrepareTableView(TableView<PathWrapper> tableContainer, TableColumn<PathWrapper, String> selectableColumn, MARTiffViewport imageViewport){
        selectableColumn.setCellValueFactory(new PropertyValueFactory<>("pathTail"));
        SetTableViewChangeListeners(tableContainer, imageViewport);
    }

    private void SetTableViewChangeListeners(TableView<PathWrapper> tableObject, MARTiffViewport imageViewport){
        tableObject.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableObject.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(Change<? extends Integer> change)
            {
                try {
                    imageViewport.RenderImageFromFile(tableObject.getSelectionModel().selectedItemProperty().get());
                    SubtractImages();
                }
                catch (IOException ex){
                    System.out.println("Image file could not be read!");
                }
            }
        });
    }

    private void SubtractImages() throws IOException{
        MARTiffImage resultantImage = DataSubtraction.SubtractImages(selectedImageViewport.getController().getCachedImage(), subtractedImageViewport.getController().getCachedImage(), true);
        resultantImageViewport.RenderImage(resultantImage);
    }
}
