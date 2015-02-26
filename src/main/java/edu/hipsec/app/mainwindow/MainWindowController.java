package edu.hipsec.app.mainwindow;

import edu.hipsec.app.workspaces.bulkimagecorrection.BulkImageCorrection;
import edu.hipsec.app.workspaces.singleimagecorrection.SingleImageCorrection;
import edu.hipsec.app.workspaces.singleimageviewer.SingleImageViewer;
import com.quantumjockey.dialogs.DirectoryChooserWrapper;
import com.quantumjockey.mvvmbase.dialogs.AlertWindow;
import com.quantumjockey.mvvmbase.window.WindowControllerBase;
import com.quantumjockey.parsers.DirectoryParser;
import com.quantumjockey.paths.SystemAttributes;
import javafx.fxml.FXML;
import com.quantumjockey.paths.PathWrapper;
import com.quantumjockey.paths.filters.FilterWrapper;
import xrdtiffoperations.imagemodel.FileExtensions;
import java.io.*;
import java.util.*;

public class MainWindowController extends WindowControllerBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    @FXML
    private BulkImageCorrection multipleImageWorkspace;

    @FXML
    private SingleImageCorrection singleImageWorkspace;

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

            if (availableFiles.size() != 0) {
                String path = selectedDirectory.getPath();
                multipleImageWorkspace.getController().updateControls(availableFiles, path);
                singleImageWorkspace.getController().updateControls(availableFiles, path);
                singleImageViewer.getController().updateControls(availableFiles, path);
            } else {
                String title = "No Files Found";
                String message = "No files matching XRD file extension filters have been found in this directory. If files exist in a subdirectory of this   " + SystemAttributes.LINE_SEPARATOR
                        + "folder, that subdirectory must be explicitly selected in order for files to be parsed appropriately.";
                AlertWindow emptyDirectoryAlert = new AlertWindow(title, message);
                emptyDirectoryAlert.show();
            }
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void performInitializationTasks() {

    }

}
