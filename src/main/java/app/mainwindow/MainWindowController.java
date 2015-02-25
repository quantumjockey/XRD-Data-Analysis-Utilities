package app.mainwindow;

import app.workspaces.bulkimagecorrection.BulkImageSubtractor;
import app.workspaces.singleimagecorrection.SingleImageSubtractor;
import app.workspaces.singleimageviewer.SingleImageViewer;
import com.quantumjockey.dialogs.DirectoryChooserWrapper;
import com.quantumjockey.mvvmbase.window.WindowControllerBase;
import com.quantumjockey.parsers.DirectoryParser;
import javafx.fxml.FXML;
import com.quantumjockey.paths.PathWrapper;
import com.quantumjockey.paths.filters.FilterWrapper;
import xrdtiffoperations.imagemodel.FileExtensions;
import java.io.*;
import java.util.*;

public class MainWindowController extends WindowControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private BulkImageSubtractor multipleImageWorkspace;

    @FXML
    private SingleImageSubtractor singleImageWorkspace;

    @FXML
    private SingleImageViewer singleImageViewer;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void exitApplication() {
        System.exit(0);
    }

    @FXML
    public void getDirectoryToDisplay() {
        ArrayList<PathWrapper> availableFiles;
        DirectoryChooserWrapper dialog;

        dialog = new DirectoryChooserWrapper("Select Directory for Images");
        File selectedDirectory = dialog.getSelectedDirectory();

        if (selectedDirectory != null) {
            availableFiles = DirectoryParser.parseSelectedDirectory(selectedDirectory, new FilterWrapper(new String[]{FileExtensions.DEFAULT, FileExtensions.EXTENDED_DEFAULT, FileExtensions.MAR_2300, FileExtensions.MAR_3450}));
            String path = selectedDirectory.getPath();
            multipleImageWorkspace.getController().updateControls(availableFiles, path);
            singleImageWorkspace.getController().updateControls(availableFiles, path);
            singleImageViewer.getController().updateControls(availableFiles, path);
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        multipleImageWorkspace = new BulkImageSubtractor();
        singleImageWorkspace = new SingleImageSubtractor();
        singleImageViewer = new SingleImageViewer();
    }

    @Override
    protected void performInitializationTasks() {

    }

}
