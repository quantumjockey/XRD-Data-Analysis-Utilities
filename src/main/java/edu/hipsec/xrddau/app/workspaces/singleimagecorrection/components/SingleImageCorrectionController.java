package edu.hipsec.xrddau.app.workspaces.singleimagecorrection.components;

import edu.hipsec.xrddau.app.controls.filegroupselector.FileGroupSelector;
import edu.hipsec.xrddau.app.controls.diffractionframeworkspace.DiffractionFrameWorkspace;
import edu.hipsec.xrddau.app.filesystem.FileSysReader;
import edu.hipsec.xrddau.app.workspaces.WorkspaceController;
import com.quantumjockey.paths.PathWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.quantumjockey.mvvmbase.controls.initialization.LabelExt;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.math.DataSubtraction;

import java.io.IOException;
import java.util.ArrayList;

public class SingleImageCorrectionController extends MarkupControllerBase implements WorkspaceController {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private FileGroupSelector backgroundImagePath;

    @FXML
    private FileGroupSelector diffractionImagePath;

    @FXML
    private DiffractionFrameWorkspace resultantImageViewport;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;
    private DiffractionFrame backgroundImage;
    private DiffractionFrame diffractionImage;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root) {
        availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        ChangeListener<TreeItem<String>> selectedChanged = createSelectedListener();
        diffractionImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);

        ChangeListener<TreeItem<String>> subtractedChanged = createSubtractedListener();
        backgroundImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, subtractedChanged);

        LabelExt.update(rootPath, root, root);

        try {
            diffractionImage = FileSysReader.readImageData(availableFiles.get(diffractionImagePath.getController().getSelectionModel().getSelectedIndex()));
            backgroundImage = FileSysReader.readImageData(availableFiles.get(backgroundImagePath.getController().getSelectionModel().getSelectedIndex()));
            subtractImages();
        } catch (IOException ex) {
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createSelectedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = diffractionImagePath.getController().getSelectionModel();
                if (selected != null
                        && !selected.isEmpty()
                        && newValue != null
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0) {
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    diffractionImagePath.getController().setTooltip(new Tooltip(tip));
                    diffractionImage = FileSysReader.readImageData(getPath(newValue.getValue()));
                    subtractImages();
                }
            } catch (IOException ex) {
                System.out.println("Image file could not be read!");
            }
        };
    }

    private ChangeListener<TreeItem<String>> createSubtractedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = backgroundImagePath.getController().getSelectionModel();
                if (selected != null
                        && !selected.isEmpty()
                        && newValue != null
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0) {
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    backgroundImagePath.getController().setTooltip(new Tooltip(tip));
                    backgroundImage = FileSysReader.readImageData(getPath(newValue.getValue()));
                    subtractImages();
                }
            } catch (IOException ex) {
                System.out.println("Image file could not be read!");
            }
        };
    }

    private PathWrapper getPath(String file) {
        PathWrapper path = null;
        for (PathWrapper item : availableFiles)
            if (item.getPathTail().equals(file)) {
                path = item;
                break;
            }
        return path;
    }

    private void subtractImages() throws IOException {
        DiffractionFrame resultantImage = DataSubtraction.subtractImages(backgroundImage, diffractionImage);
        resultantImageViewport.renderImage(resultantImage);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {

    }

    @Override
    protected void setDefaults() {
        String rootDefault = "(Unspecified)";
        LabelExt.update(rootPath, rootDefault, null);
        diffractionImagePath.getController().setHeader("Inspected Image:");
        backgroundImagePath.getController().setHeader("Background Image:");
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
