package app.workspaces.singleimageviewer.components;

import app.controls.filegroupselector.FileGroupSelector;
import app.controls.martiffviewport.MARTiffViewport;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.macros.LabelExt;
import mvvmbase.markup.MarkupControllerBase;
import paths.PathWrapper;
import java.io.IOException;
import java.util.ArrayList;

public class SingleImageViewerController  extends MarkupControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private MARTiffViewport selectedImageViewport;

    @FXML
    private FileGroupSelector selectedPath;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        ChangeListener<TreeItem<String>> selectedChanged = createListener(selectedImageViewport);
        selectedPath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);
        LabelExt.update(rootPath, root, root);
        try{
            selectedImageViewport.renderImageFromFile(availableFiles.get(selectedPath.getController().getSelectionModel().getSelectedIndex()));
        }
        catch (IOException ex){
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createListener(MARTiffViewport imageViewport) {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = selectedPath.getController().getSelectionModel();
                if (!selected.isEmpty()
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0){
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    selectedPath.getController().setTooltip(new Tooltip(tip));
                    imageViewport.renderImageFromFile(getPath(newValue.getValue()));
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

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        selectedPath = new FileGroupSelector();
        selectedImageViewport = new MARTiffViewport();
    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
        selectedPath.getController().setHeader("Image Selected for Viewing");
    }

    @Override
    protected void setListeners(){

    }

}
