package app.workspaces.singleimagecorrection.components;

import app.controls.martiffviewport.MARTiffViewport;
import mvvmbase.controls.macros.TreeViewExt;
import paths.PathWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.macros.ComboBoxExt;
import mvvmbase.controls.macros.LabelExt;
import mvvmbase.markup.MarkupControllerBase;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.math.DataSubtraction;

import java.io.IOException;
import java.util.ArrayList;

public class SingleImageSubtractorController extends MarkupControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private MARTiffViewport selectedImageViewport;

    @FXML
    private MARTiffViewport subtractedImageViewport;

    @FXML
    private MARTiffViewport resultantImageViewport;

    @FXML
    private TreeView<String> selectedPath;

    @FXML
    private TreeView<String> subtractedPath;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        ChangeListener<TreeItem<String>> selectedChanged = createListener(selectedPath, selectedImageViewport);
        TreeViewExt.populateTree(selectedPath, temp, root, SelectionMode.SINGLE, false, null, selectedChanged);
        ChangeListener<TreeItem<String>> subtractedChanged = createListener(subtractedPath, subtractedImageViewport);
        TreeViewExt.populateTree(subtractedPath, temp, root, SelectionMode.SINGLE, false, null, subtractedChanged);
        LabelExt.update(rootPath, root, root);
        try{
            selectedImageViewport.renderImageFromFile(availableFiles.get(selectedPath.getSelectionModel().getSelectedIndex()));
            subtractedImageViewport.renderImageFromFile(availableFiles.get(subtractedPath.getSelectionModel().getSelectedIndex()));
            subtractImages();
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createListener(TreeView<String> selector, MARTiffViewport imageViewport) {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel selected = selector.getSelectionModel();
                if (!selectedPath.getSelectionModel().isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selector.getSelectionModel().getSelectedItem().getValue();
                    selector.setTooltip(new Tooltip(tip));
                    imageViewport.renderImageFromFile(getPath(newValue.getValue()));
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
        MARTiffImage resultantImage = DataSubtraction.subtractImages(selectedImageViewport.getController().getCachedImage(), subtractedImageViewport.getController().getCachedImage());
        resultantImageViewport.renderImage(resultantImage);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        selectedImageViewport = new MARTiffViewport();
        subtractedImageViewport = new MARTiffViewport();
        resultantImageViewport = new MARTiffViewport();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        selectedImageViewport.getController().setViewportTitle("Selected Image");
        subtractedImageViewport.getController().setViewportTitle("Subtracted Image");
        resultantImageViewport.getController().setViewportTitle("Resultant Image");
        LabelExt.update(rootPath, rootDefault, null);
    }

    @Override
    protected void setListeners(){

    }

}
