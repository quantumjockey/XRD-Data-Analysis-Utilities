package app.multipleimagesubtractor.components;

import app.filesystem.FileSysReader;
import app.filesystem.FileSysWriter;
import mvvmbase.dialogs.AlertWindow;
import mvvmbase.dialogs.wrappers.DirectoryChooserWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.ComboBoxExt;
import mvvmbase.controls.LabelExt;
import mvvmbase.controls.ListViewExt;
import mvvmbase.markup.MarkupControllerBase;
import pathoperations.PathWrapper;
import pathoperations.SystemAttributes;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataSubtraction;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MultipleImageSubtractorController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ListView<String> selectedPath;

    @FXML
    private ComboBox<String> subtractedPath;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void subtractImageGroup() throws IOException {
        if (selectedPath.getSelectionModel().isEmpty()){
            AlertWindow alert = new AlertWindow("Invalid Operation", "No images are selected for subtraction.");
            alert.show();
        }
        else {
            DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Save to...");
            File destination = dialog.getSelectedDirectory();
            if (destination != null) {
                ArrayList<PathWrapper> selected = getSelectedPaths();
                MARTiffImage subtracted = getSubtractedImageData();
                streamImageSubtraction(destination, selected, subtracted);
            }
        }
    }

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        ListViewExt.populate(selectedPath, temp, null, SelectionMode.MULTIPLE, false);
        ComboBoxExt.populate(subtractedPath, temp, createListener(subtractedPath));
        LabelExt.update(rootPath, root, root);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<String> createListener(ComboBox<String> selector) {
        return (observable, oldValue, newValue) -> selector.setTooltip(new Tooltip(selector.getSelectionModel().getSelectedItem()));
    }

    private ArrayList<PathWrapper> getSelectedPaths(){
        ArrayList<PathWrapper> selectedPaths = new ArrayList<>();
        selectedPath.getSelectionModel().getSelectedIndices().forEach((index) -> selectedPaths.add(availableFiles.get(index)));
        return selectedPaths;
    }

    private MARTiffImage getSubtractedImageData() throws IOException{
        PathWrapper subtracted = availableFiles.get(subtractedPath.getSelectionModel().getSelectedIndex());
        return FileSysReader.readImageData(subtracted);
    }

    private void streamImageSubtraction(File destination, ArrayList<PathWrapper> selectedPaths, MARTiffImage subtractedImage){
        selectedPaths.forEach((path) -> {
            MARTiffImage firstImage = null;
            try {
                firstImage = FileSysReader.readImageData(path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (firstImage != null && subtractedImage != null) {
                MARTiffImage result = DataSubtraction.subtractImages(firstImage, subtractedImage);
                String filePath = destination.getPath() + SystemAttributes.FILE_SEPARATOR + result.filename;
                FileSysWriter.writeImageData(new File(filePath), result);
            }
        });
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
    }

    @Override
    protected void setListeners(){

    }

}