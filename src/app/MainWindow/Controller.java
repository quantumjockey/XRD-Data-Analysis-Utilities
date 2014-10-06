package app.MainWindow;

import DialogInitialization.DirectoryChooserWrapper;
import MvvmBase.window.WindowControllerBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import pathoperations.PathWrapper;
import pathoperations.filters.FilterWrapper;
import xrdtiffoperations.readers.filewrappers.TiffReader;
import xrdtiffvisualization.MARTiffVisualizer;

import java.io.*;
import java.util.*;


public class Controller extends WindowControllerBase{

    // Main Viewer
    @FXML private ImageView selectedImageViewport;

    // Subtracted Viewer
    @FXML private ImageView subtractedImageViewport;

    // Result Viewer
    @FXML private ImageView subtractionResultViewport;

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
    }

    private void PopulateTableView(TableView<PathWrapper> tableControl, TableColumn<PathWrapper, String> columnControl, ArrayList<PathWrapper> paths, ImageView imageViewport){
        tableControl.setItems(FXCollections.observableList(paths));
        tableControl.getSelectionModel().select(0);
        PrepareTableView(tableControl, columnControl, imageViewport);
        try {
            RenderImage(tableControl.getSelectionModel().selectedItemProperty().get(), imageViewport);
        }
        catch (IOException ex){
            System.out.println("Image file could not be read!");
        }
    }

    private void PrepareTableView(TableView<PathWrapper> tableContainer, TableColumn<PathWrapper, String> selectableColumn, ImageView imageViewport){
        selectableColumn.setCellValueFactory(new PropertyValueFactory<>("pathTail"));
        SetTableViewChangeListeners(tableContainer, imageViewport);
    }

    private void RenderImage(PathWrapper imagePath, ImageView viewport) throws IOException{
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(imagePath.getInjectedPath());
            marImageReader.ReadFileData(false);
            MARTiffVisualizer marImageGraph = new MARTiffVisualizer(marImageReader.GetImageData());
            viewport.setImage(marImageGraph.RenderDataAsImage(false));
        }
    }

    private void SetTableViewChangeListeners(TableView<PathWrapper> tableObject, ImageView imageViewport){
        tableObject.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableObject.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(Change<? extends Integer> change)
            {
                try {
                    RenderImage(tableObject.getSelectionModel().selectedItemProperty().get(), imageViewport);
                }
                catch (IOException ex){
                    System.out.println("Image file could not be read!");
                }
            }
        });
    }

}
