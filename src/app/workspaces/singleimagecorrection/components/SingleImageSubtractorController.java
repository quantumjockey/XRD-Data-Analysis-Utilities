package app.workspaces.singleimagecorrection.components;

import app.controls.filegroupselector.FileGroupSelector;
import app.controls.martiffviewport.MARTiffViewport;
import app.filesystem.FileSysReader;
import paths.PathWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.macros.LabelExt;
import mvvmbase.markup.MarkupControllerBase;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataSubtraction;

import java.io.IOException;
import java.util.ArrayList;

public class SingleImageSubtractorController extends MarkupControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private MARTiffViewport resultantImageViewport;

    @FXML
    private FileGroupSelector selectedPath;

    @FXML
    private FileGroupSelector subtractedPath;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;
    private MARTiffImage selectedImage;
    private MARTiffImage subtractedImage;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        ChangeListener<TreeItem<String>> selectedChanged = createSelectedListener();
        selectedPath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);

        ChangeListener<TreeItem<String>> subtractedChanged = createSubtractedListener();
        subtractedPath.getController().populateTree(temp, root, SelectionMode.SINGLE, subtractedChanged);

        LabelExt.update(rootPath, root, root);

        try{
            selectedImage = FileSysReader.readImageData(availableFiles.get(selectedPath.getController().getSelectionModel().getSelectedIndex()));
            subtractedImage = FileSysReader.readImageData(availableFiles.get(subtractedPath.getController().getSelectionModel().getSelectedIndex()));
            subtractImages();
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createSelectedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = selectedPath.getController().getSelectionModel();
                if (!selected.isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    selectedPath.getController().setTooltip(new Tooltip(tip));
                    selectedImage = FileSysReader.readImageData(getPath(newValue.getValue()));
                    subtractImages();
                }
            }
            catch (IOException ex){
                System.out.println("Image file could not be read!");
            }
        };
    }

    private ChangeListener<TreeItem<String>> createSubtractedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = subtractedPath.getController().getSelectionModel();
                if (!selected.isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    subtractedPath.getController().setTooltip(new Tooltip(tip));
                    subtractedImage = FileSysReader.readImageData(getPath(newValue.getValue()));
                    subtractImages();
                }
            }
            catch (IOException ex){
                System.out.println("Image file could not be read!");
            }
        };
    }

    private PathWrapper getPath(String file){
        PathWrapper path = null;
        for (PathWrapper item : availableFiles){
            if (item.getPathTail().equals(file)){
                path = item;
                break;
            }
        }
        return path;
    }

    private void subtractImages() throws IOException{
        MARTiffImage resultantImage = DataSubtraction.subtractImages(subtractedImage, selectedImage);
        resultantImageViewport.renderImage(resultantImage);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        resultantImageViewport = new MARTiffViewport();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
        selectedPath.getController().setHeader("Inspected Image:");
        subtractedPath.getController().setHeader("Dark Field Image:");
    }

    @Override
    protected void setListeners(){

    }

}
