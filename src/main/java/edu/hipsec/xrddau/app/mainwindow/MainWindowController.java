package edu.hipsec.xrddau.app.mainwindow;

import edu.hipsec.xrddau.app.workspaces.bulkimagecorrection.BulkImageCorrection;
import edu.hipsec.xrddau.app.workspaces.singleimagecorrection.SingleImageCorrection;
import edu.hipsec.xrddau.app.workspaces.singleimageviewer.SingleImageViewer;
import com.quantumjockey.filesystem.dialogs.DirectoryChooserWrapper;
import com.quantumjockey.melya.dialogs.AlertWindow;
import com.quantumjockey.melya.window.WindowControllerBase;
import com.quantumjockey.filesystem.operators.DirectoryOperator;
import com.quantumjockey.filesystem.paths.SystemAttributes;
import com.quantumjockey.filesystem.paths.PathWrapper;
import com.quantumjockey.filesystem.paths.filters.FilterWrapper;
import edu.hipsec.xrdtiffoperations.constants.FileExtensions;
import java.io.*;
import java.util.*;
import javafx.fxml.FXML;

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

            availableFiles = new ArrayList<>(
                    Arrays.asList(DirectoryOperator.parseSelectedDirectory(
                                    selectedDirectory, new FilterWrapper(
                                            new String[]{FileExtensions.DEFAULT, FileExtensions.EXTENDED_DEFAULT, FileExtensions.MAR_2300, FileExtensions.MAR_3450}
                                    )
                            )
                    )
            );

            if (availableFiles.size() != 0) {
                String path = selectedDirectory.getPath();
                this.multipleImageWorkspace.getController().updateControls(availableFiles, path);
                this.singleImageWorkspace.getController().updateControls(availableFiles, path);
                this.singleImageViewer.getController().updateControls(availableFiles, path);
            } else {
                String title = "No Files Found";
                String message = "No files matching XRD file extension filters have been found in this directory. If files exist in a subdirectory of this   " + SystemAttributes.LineSeparator()
                        + "folder, that subdirectory must be explicitly selected in order for files to be parsed appropriately.";
                AlertWindow emptyDirectoryAlert = new AlertWindow(title, message);
                emptyDirectoryAlert.show();
            }
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks() {

    }

}
