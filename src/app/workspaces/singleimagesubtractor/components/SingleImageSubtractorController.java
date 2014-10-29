package app.workspaces.singleimagesubtractor.components;

import app.controls.martiffviewport.MARTiffViewport;
import filesystembase.paths.PathWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mvvmbase.controls.ComboBoxExt;
import mvvmbase.controls.LabelExt;
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
    private ComboBox<String> selectedPath;

    @FXML
    private ComboBox<String> subtractedPath;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root){
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));
        ChangeListener<String> selectedChanged = createListener(selectedPath, selectedImageViewport);
        ComboBoxExt.populate(selectedPath, temp, selectedChanged);
        ChangeListener<String> subtractedChanged = createListener(subtractedPath, subtractedImageViewport);
        ComboBoxExt.populate(subtractedPath, temp, subtractedChanged);
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

    private ChangeListener<String> createListener(ComboBox<String> selector, MARTiffViewport imageViewport) {
        return (observable, oldValue, newValue) -> {
            try {
                SingleSelectionModel selected = selector.getSelectionModel();
                if (selected.getSelectedIndex() >= 0) {
                    selector.setTooltip(new Tooltip(selector.getSelectionModel().getSelectedItem()));
                    imageViewport.renderImageFromFile(availableFiles.get(selected.getSelectedIndex()));
                    subtractImages();
                }
            }
            catch (IOException ex){
                System.out.println("Image file could not be read!");
            }
        };
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
