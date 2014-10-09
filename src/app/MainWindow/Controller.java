package app.MainWindow;

import DialogInitialization.DirectoryChooserWrapper;
import DialogInitialization.FileSaveChooserWrapper;
import MvvmBase.window.WindowControllerBase;
import xrdtiffoperations.math.DataSubtraction;
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
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.wrappers.filewrappers.TiffReader;
import xrdtiffoperations.wrappers.filewrappers.TiffWriter;
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
    private MARTiffImage selectedImage;
    private MARTiffImage subtractedImage;
    private MARTiffImage subtractionResult;

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

    @FXML
    public void ExportSubtractedImage(){
        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        dialog.SetInitialFileName(subtractionResult.getFilename());
        File destination = dialog.GetSaveDirectory();
        if (destination != null) {
            TiffWriter writer = new TiffWriter(subtractionResult);
            writer.Write(destination.getPath());
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void CacheImage(TableView<PathWrapper> tableObject, MARTiffImage image){
        if (tableObject == availableImages && image != null){
            selectedImage = image;
        }
        else{
            subtractedImage = image;
        }
    }

    private void ParseSelectedDirectory() throws IOException{

        FilterWrapper tiffFilter = new FilterWrapper(new String[]{".tif", ".tiff"});
        File[] images = selectedDirectory.listFiles(tiffFilter.filter);
        ArrayList<PathWrapper> imagesPaths = new ArrayList<>();

        for (File item : images){
            PathWrapper wrapper = new PathWrapper(item.getPath());
            imagesPaths.add(wrapper);
        }

        PopulateTableView(availableImages, selectedPath, imagesPaths, selectedImageViewport, selectedImage);
        PopulateTableView(subtractedImages, subtractedPath, imagesPaths, subtractedImageViewport, subtractedImage);
        try{
            selectedImage = ReadImageData(availableImages.getSelectionModel().selectedItemProperty().get());
            RenderImage(selectedImage, selectedImageViewport);
            subtractedImage = ReadImageData(subtractedImages.getSelectionModel().selectedItemProperty().get());
            RenderImage(subtractedImage, subtractedImageViewport);
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
        SubtractImages();
    }

    private void PopulateTableView(TableView<PathWrapper> tableControl, TableColumn<PathWrapper, String> columnControl, ArrayList<PathWrapper> paths, ImageView imageViewport, MARTiffImage image){
        tableControl.setItems(FXCollections.observableList(paths));
        tableControl.getSelectionModel().select(0);
        PrepareTableView(tableControl, columnControl, imageViewport);
        CacheImage(tableControl, image);
    }

    private void PrepareTableView(TableView<PathWrapper> tableContainer, TableColumn<PathWrapper, String> selectableColumn, ImageView imageViewport){
        selectableColumn.setCellValueFactory(new PropertyValueFactory<>("pathTail"));
        SetTableViewChangeListeners(tableContainer, imageViewport);
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

    private void RenderImage(MARTiffImage image, ImageView viewport) throws IOException{
            MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
            viewport.setImage(marImageGraph.RenderDataAsImage(false));
    }

    private void SetTableViewChangeListeners(TableView<PathWrapper> tableObject, ImageView imageViewport){
        tableObject.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableObject.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(Change<? extends Integer> change)
            {
                try {
                    MARTiffImage image = ReadImageData(tableObject.getSelectionModel().selectedItemProperty().get());
                    CacheImage(tableObject, image);
                    RenderImage(image, imageViewport);
                    SubtractImages();
                }
                catch (IOException ex){
                    System.out.println("Image file could not be read!");
                }
            }
        });
    }

    private void SubtractImages() throws IOException{
        subtractionResult = DataSubtraction.SubtractImages(selectedImage, subtractedImage, true);
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(subtractionResult);
        subtractionResultViewport.setImage(marImageGraph.RenderDataAsImage(false));
    }
}
