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
        this.availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        this.availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        ChangeListener<TreeItem<String>> selectedChanged = this.createSelectedListener();
        this.diffractionImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);

        ChangeListener<TreeItem<String>> subtractedChanged = this.createSubtractedListener();
        this. backgroundImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, subtractedChanged);

        LabelExt.update(this.rootPath, root, root);

        try {
            this.diffractionImage = FileSysReader.readImageData(this.availableFiles.get(this.diffractionImagePath.getController().getSelectionModel().getSelectedIndex()));
            this.backgroundImage = FileSysReader.readImageData(this.availableFiles.get(this.backgroundImagePath.getController().getSelectionModel().getSelectedIndex()));
            this.subtractImages();
        } catch (IOException ex) {
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createSelectedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = this.diffractionImagePath.getController().getSelectionModel();
                if (selected != null
                        && !selected.isEmpty()
                        && newValue != null
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0) {
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    this.diffractionImagePath.getController().setTooltip(new Tooltip(tip));
                    this.diffractionImage = FileSysReader.readImageData(this.getPath(newValue.getValue()));
                    this.subtractImages();
                }
            } catch (IOException ex) {
                System.out.println("Image file could not be read!");
            }
        };
    }

    private ChangeListener<TreeItem<String>> createSubtractedListener() {
        return (observable, oldValue, newValue) -> {
            try {
                MultipleSelectionModel<TreeItem<String>> selected = this.backgroundImagePath.getController().getSelectionModel();
                if (selected != null
                        && !selected.isEmpty()
                        && newValue != null
                        && newValue.isLeaf()
                        && selected.getSelectedIndex() >= 0) {
                    String tip = "Current Selection: " + selected.getSelectedItem().getValue();
                    this.backgroundImagePath.getController().setTooltip(new Tooltip(tip));
                    this.backgroundImage = FileSysReader.readImageData(this.getPath(newValue.getValue()));
                    this.subtractImages();
                }
            } catch (IOException ex) {
                System.out.println("Image file could not be read!");
            }
        };
    }

    private PathWrapper getPath(String file) {
        PathWrapper path = null;
        for (PathWrapper item : this.availableFiles)
            if (item.getPathTail().equals(file)) {
                path = item;
                break;
            }
        return path;
    }

    private void subtractImages() throws IOException {
        DiffractionFrame resultantImage = DataSubtraction.subtractImages(this.backgroundImage, this.diffractionImage);
        this.resultantImageViewport.renderImage(resultantImage);
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
        LabelExt.update(this.rootPath, rootDefault, null);
        this.diffractionImagePath.getController().setHeader("Inspected Image:");
        this.backgroundImagePath.getController().setHeader("Background Image:");
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
