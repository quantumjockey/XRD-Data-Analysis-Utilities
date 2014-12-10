package app.workspaces.bulkimagecorrection.components;

import app.controls.filegroupselector.FileGroupSelector;
import app.filesystem.FileSysReader;
import app.filesystem.FileSysWriter;
import app.workspaces.WorkspaceController;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mvvmbase.controls.initialization.LabelExt;
import mvvmbase.dialogs.AlertWindow;
import dialogs.DirectoryChooserWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.markup.MarkupControllerBase;
import paths.PathWrapper;
import paths.SystemAttributes;
import xrdtiffoperations.imagemodel.FileTypes;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataMasking;
import xrdtiffoperations.math.DataSubtraction;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BulkImageSubtractorController extends MarkupControllerBase implements WorkspaceController {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private FileGroupSelector darkFieldImagePath;

    @FXML
    private FileGroupSelector diffractionImagePath;

    @FXML
    private Label rootPath;

    @FXML
    private TextField lowerBoundFilter;

    @FXML
    private TextField upperBoundFilter;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void subtractImageGroup() throws IOException {
        if (diffractionImagePath.getController().getSelectionModel().isEmpty()
                || !diffractionImagePath.getController().getSelectionModel().getSelectedItem().isLeaf()
                || darkFieldImagePath.getController().getSelectionModel().isEmpty()
                || !darkFieldImagePath.getController().getSelectionModel().getSelectedItem().isLeaf()){
            AlertWindow alert = new AlertWindow("Invalid Operation", "No images are selected for subtraction.");
            alert.show();
        }
        else {
            DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Save to...");
            File destination = dialog.getSelectedDirectory();
            if (destination != null) {
                ArrayList<PathWrapper> selected = getSelectedPaths();
                MARTiffImage result = getSubtractedImageData();
                streamImageSubtraction(destination, selected, result);
            }
        }
    }

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        diffractionImagePath.getController().populateTree(temp, root, SelectionMode.MULTIPLE, null);
        darkFieldImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, null);
        LabelExt.update(rootPath, root, root);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ArrayList<PathWrapper> getSelectedPaths(){
        ArrayList<PathWrapper> selectedPaths = new ArrayList<>();
        diffractionImagePath.getController().getSelectionModel().getSelectedItems().forEach((item) -> {
            if (item.isLeaf()) {
                availableFiles.forEach((path) -> {
                    if (path.getPathTail().equals(item.getValue())){
                        selectedPaths.add(availableFiles.get(availableFiles.indexOf(path)));
                    }
                });
            }
        });
        return selectedPaths;
    }

    private MARTiffImage getSubtractedImageData() throws IOException{
        PathWrapper subtracted = null;
        for (PathWrapper file : availableFiles){
            if (file.getPathTail().contains(darkFieldImagePath.getController().getSelectionModel().getSelectedItem().getValue())){
                subtracted = file;
            }
        }
        return FileSysReader.readImageData(subtracted);
    }

    private MARTiffImage filterImage(MARTiffImage image){
        MARTiffImage result = image;
        try {
            int lowerBound = result.getMinValue();
            int upperBound = result.getMaxValue();
            if (!lowerBoundFilter.getText().isEmpty()) {
                lowerBound = Integer.parseInt(lowerBoundFilter.getText().trim());
            }
            if (!upperBoundFilter.getText().isEmpty()) {
                upperBound = Integer.parseInt(upperBoundFilter.getText().trim());
            }
            result = DataMasking.maskImage(result, lowerBound, upperBound);
        }
        catch (Exception ex){
            ex.printStackTrace();
            result = image;
        }
        return result;
    }

    private void streamImageSubtraction(File destination, ArrayList<PathWrapper> selectedPaths, MARTiffImage subtractedImage){
        String basePath = destination.getPath();

        String[] parts = selectedPaths.get(0).getPathTail().split("_");
        String newDirectoryName = parts[0] + "_" + parts[1];
        String newDestination = basePath + SystemAttributes.FILE_SEPARATOR + newDirectoryName;

        try {
            if (!Files.exists(Paths.get(newDestination))) {
                Files.createDirectory(Paths.get(newDestination));
            }
            else{
                int i = 1;
                while(Files.exists(Paths.get(newDestination + "(" + i + ")"))){
                    i++;
                }
                newDestination += "(" + i + ")";
                Files.createDirectory(Paths.get(newDestination));
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        final String newerDestination = newDestination;

        selectedPaths.forEach((path) -> {
            MARTiffImage baseImage = null;

            try {
                baseImage = FileSysReader.readImageData(path);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            if (baseImage != null && subtractedImage != null) {
                MARTiffImage result = DataSubtraction.subtractImages(subtractedImage, baseImage);
                result = filterImage(result);
                String filePath = newerDestination + SystemAttributes.FILE_SEPARATOR + result.getFilename();
                FileSysWriter.writeImageData(new File(filePath), result, FileTypes.TIFF_32_BIT_INT);
            }
        });
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        diffractionImagePath = new FileGroupSelector();
        darkFieldImagePath = new FileGroupSelector();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
        diffractionImagePath.getController().setHeader("Image(s) Selected for Correction");
        darkFieldImagePath.getController().setHeader("Dark Field Image");
    }

    @Override
    protected void setListeners(){

    }

}
