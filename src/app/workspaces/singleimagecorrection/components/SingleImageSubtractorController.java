package app.workspaces.singleimagecorrection.components;

import app.controls.filegroupselector.FileGroupSelector;
import app.controls.martiffviewport.MARTiffViewport;
import app.filesystem.FileSysReader;
import app.workspaces.WorkspaceController;
import paths.PathWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.initialization.LabelExt;
import mvvmbase.markup.MarkupControllerBase;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataSubtraction;

import java.io.IOException;
import java.util.ArrayList;

public class SingleImageSubtractorController extends MarkupControllerBase implements WorkspaceController {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private FileGroupSelector darkFieldImagePath;

    @FXML
    private FileGroupSelector diffractionImagePath;

    @FXML
    private MARTiffViewport resultantImageViewport;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;
    private MARTiffImage darkFieldImage;
    private MARTiffImage diffractionImage;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        ChangeListener<TreeItem<String>> selectedChanged = createSelectedListener();
        diffractionImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);

        ChangeListener<TreeItem<String>> subtractedChanged = createSubtractedListener();
        darkFieldImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, subtractedChanged);

        LabelExt.update(rootPath, root, root);

        try{
            diffractionImage = FileSysReader.readImageData(availableFiles.get(diffractionImagePath.getController().getSelectionModel().getSelectedIndex()));
            darkFieldImage = FileSysReader.readImageData(availableFiles.get(darkFieldImagePath.getController().getSelectionModel().getSelectedIndex()));
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
                MultipleSelectionModel<TreeItem<String>> selected = diffractionImagePath.getController().getSelectionModel();
                if (!selected.isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    diffractionImagePath.getController().setTooltip(new Tooltip(tip));
                    diffractionImage = FileSysReader.readImageData(getPath(newValue.getValue()));
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
                MultipleSelectionModel<TreeItem<String>> selected = darkFieldImagePath.getController().getSelectionModel();
                if (!selected.isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    darkFieldImagePath.getController().setTooltip(new Tooltip(tip));
                    darkFieldImage = FileSysReader.readImageData(getPath(newValue.getValue()));
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
        MARTiffImage resultantImage = DataSubtraction.subtractImages(darkFieldImage, diffractionImage);
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
        diffractionImagePath.getController().setHeader("Inspected Image:");
        darkFieldImagePath.getController().setHeader("Dark Field Image:");
    }

    @Override
    protected void setListeners(){

    }

}
