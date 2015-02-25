package app.workspaces.bulkimagecorrection.components;

import app.controls.filegroupselector.FileGroupSelector;
import app.filesystem.FileSysReader;
import app.filesystem.FileSysWriter;
import app.workspaces.WorkspaceController;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.quantumjockey.mvvmbase.controls.initialization.LabelExt;
import com.quantumjockey.mvvmbase.dialogs.AlertWindow;
import com.quantumjockey.dialogs.DirectoryChooserWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import com.quantumjockey.paths.PathWrapper;
import com.quantumjockey.paths.SystemAttributes;
import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.FileExtensions;
import xrdtiffoperations.imagemodel.FileTypes;
import xrdtiffoperations.math.DataMasking;
import xrdtiffoperations.math.DataSubtraction;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BulkImageSubtractorController extends MarkupControllerBase implements WorkspaceController {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private FileGroupSelector backgroundImagePath;

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
                || backgroundImagePath.getController().getSelectionModel().isEmpty()
                || !backgroundImagePath.getController().getSelectionModel().getSelectedItem().isLeaf()){
            AlertWindow alert = new AlertWindow("Invalid Operation", "No images are selected for subtraction.");
            alert.show();
        }
        else {
            DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Save to...");
            File destination = dialog.getSelectedDirectory();
            if (destination != null) {
                ArrayList<PathWrapper> selected = getSelectedPaths();
                DiffractionFrame result = getSubtractedImageData();
                streamImageSubtraction(destination, selected, result);
            }
        }
    }

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        diffractionImagePath.getController().populateTree(temp, root, SelectionMode.MULTIPLE, null);
        backgroundImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, null);
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

    private DiffractionFrame getSubtractedImageData() throws IOException{
        PathWrapper subtracted = null;
        for (PathWrapper file : availableFiles){
            if (file.getPathTail().contains(backgroundImagePath.getController().getSelectionModel().getSelectedItem().getValue())){
                subtracted = file;
            }
        }
        return FileSysReader.readImageData(subtracted);
    }

    private void filterImage(DiffractionFrame image){
        try {
            int lowerBound = image.getMinValue();
            int upperBound = image.getMaxValue();
            if (!lowerBoundFilter.getText().isEmpty()) {
                lowerBound = Integer.parseInt(lowerBoundFilter.getText().trim());
            }
            if (!upperBoundFilter.getText().isEmpty()) {
                upperBound = Integer.parseInt(upperBoundFilter.getText().trim());
            }
            DataMasking.maskImage(image, lowerBound, upperBound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void openDirectoryInFileExplorerWindow(String directoryPath){
        try {
            Desktop.getDesktop().open(new File(directoryPath));
        }
        catch (IOException ex){
            System.out.println("File explorer window could not be opened.");
        }
    }

    private void streamImageSubtraction(File destination, ArrayList<PathWrapper> selectedPaths, DiffractionFrame backgroundImage){
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

        openDirectoryInFileExplorerWindow(newDestination);

        selectedPaths.forEach((path) -> {
            DiffractionFrame baseImage = null;

            try {
                baseImage = FileSysReader.readImageData(path);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            if (baseImage != null && backgroundImage != null) {
                DiffractionFrame result = DataSubtraction.subtractImages(backgroundImage, baseImage);
                filterImage(result);
                String filePath = newerDestination + SystemAttributes.FILE_SEPARATOR + result.getIdentifier() + FileExtensions.DEFAULT;
                FileSysWriter.writeImageData(new File(filePath), result, FileTypes.TIFF_32_BIT_INT);
            }
        });
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        diffractionImagePath = new FileGroupSelector();
        backgroundImagePath = new FileGroupSelector();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
        diffractionImagePath.getController().setHeader("Image(s) Selected for Correction");
        backgroundImagePath.getController().setHeader("Background Image");
    }

    @Override
    protected void setListeners(){

    }

}
