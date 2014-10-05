package app.MainWindow;

import DialogInitialization.DirectoryChooserWrapper;
import MvvmBase.window.WindowControllerBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
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
    @FXML private ImageView selectedImageViewPort;

    // File Listing
    @FXML private TableView<PathWrapper> availableImages;
    @FXML private TableColumn<PathWrapper, String> path;

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
        availableImages.setItems(FXCollections.observableList(imagesPaths));
        availableImages.getSelectionModel().select(0);
        RenderImage(availableImages.getSelectionModel().selectedItemProperty().get());
        PrepareTableView();
    }

    private void PrepareTableView(){
        path.setCellValueFactory(new PropertyValueFactory<>("pathTail"));
        SetTableViewChangeListeners();
    }

    private void RenderImage(PathWrapper imagePath) throws IOException{
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(imagePath.getInjectedPath());
            marImageReader.ReadFileData(false);
            MARTiffVisualizer marImageGraph = new MARTiffVisualizer(marImageReader.GetImageData());
            selectedImageViewPort.setImage(marImageGraph.RenderDataAsImage(false));
        }
    }

    private void SetTableViewChangeListeners(){
        availableImages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableImages.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(Change<? extends Integer> change)
            {
                try {
                    RenderImage(availableImages.getSelectionModel().selectedItemProperty().get());
                }
                catch (IOException ex){
                    System.out.println("Image file could not be read!");
                }
            }
        });
    }

}
