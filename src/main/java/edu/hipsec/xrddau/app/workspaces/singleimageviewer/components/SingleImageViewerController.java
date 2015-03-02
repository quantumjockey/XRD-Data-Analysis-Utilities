package edu.hipsec.xrddau.app.workspaces.singleimageviewer.components;

import edu.hipsec.xrddau.app.controls.filegroupselector.FileGroupSelector;
import edu.hipsec.xrddau.app.controls.diffractionframeworkspace.DiffractionFrameWorkspace;
import edu.hipsec.xrddau.app.workspaces.WorkspaceController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.quantumjockey.mvvmbase.controls.initialization.LabelInitializer;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import com.quantumjockey.paths.PathWrapper;
import java.io.IOException;
import java.util.ArrayList;

public class SingleImageViewerController extends MarkupControllerBase implements WorkspaceController {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private FileGroupSelector diffractionImagePath;

    @FXML
    private DiffractionFrameWorkspace diffractionImageViewport;

    @FXML
    private Label rootPath;

    private ArrayList<PathWrapper> availableFiles;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root) {
        this.availableFiles = newItems;
        ArrayList<String> temp = new ArrayList<>();
        this.availableFiles.forEach((item) -> temp.add(item.getPathTail()));

        ChangeListener<TreeItem<String>> selectedChanged = this.createListener(this.diffractionImageViewport);
        this.diffractionImagePath.getController().populateTree(temp, root, SelectionMode.SINGLE, selectedChanged);

        LabelInitializer.update(this.rootPath, root, root);

        try {
            this.diffractionImageViewport.renderImageFromFile(this.availableFiles.get(this.diffractionImagePath.getController().getSelectionModel().getSelectedIndex()));
        } catch (IOException ex) {
            System.out.println("Image file could not be rendered!");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ChangeListener<TreeItem<String>> createListener(DiffractionFrameWorkspace imageViewport) {
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
                    imageViewport.renderImageFromFile(this.getPath(newValue.getValue()));
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
        LabelInitializer.update(this.rootPath, rootDefault, null);
        this.diffractionImagePath.getController().setHeader("Image Selected for Viewing");
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
