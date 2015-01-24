package app.mainwindow;

import app.workspaces.bulkimagecorrection.BulkImageSubtractor;
import app.workspaces.singleimagecorrection.SingleImageSubtractor;
import app.workspaces.singleimageviewer.SingleImageViewer;
import com.quantumjockey.dialogs.DirectoryChooserWrapper;
import com.quantumjockey.mvvmbase.window.WindowControllerBase;
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

    private File selectedDirectory;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void exitApplication(){
        System.exit(0);
    }

    @FXML
    public void getDirectoryToDisplay(){
        ArrayList<PathWrapper> availableFiles;
        DirectoryChooserWrapper dialog;

        dialog = new DirectoryChooserWrapper("Select Directory for Images");
        selectedDirectory = dialog.getSelectedDirectory();

        if (selectedDirectory != null) {
            availableFiles = parseSelectedDirectory();
            String path = selectedDirectory.getPath();
            multipleImageWorkspace.getController().updateControls(availableFiles, path);
            singleImageWorkspace.getController().updateControls(availableFiles, path);
            singleImageViewer.getController().updateControls(availableFiles, path);
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ArrayList<PathWrapper> parseSelectedDirectory(){
        FilterWrapper tiffFilter = new FilterWrapper(new String[]{FileExtensions.DEFAULT, FileExtensions.EXTENDED_DEFAULT, FileExtensions.MAR_2300, FileExtensions.MAR_3450});
        File[] images = selectedDirectory.listFiles(tiffFilter.getFilter());
        ArrayList<PathWrapper> imagesPaths = new ArrayList<>();
        for (File item : images){
            PathWrapper wrapper = new PathWrapper(item.getPath());
            imagesPaths.add(wrapper);
        }
        return imagesPaths;
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
        multipleImageWorkspace = new BulkImageSubtractor();
        singleImageWorkspace = new SingleImageSubtractor();
        singleImageViewer = new SingleImageViewer();
    }

    @Override
    protected void performInitializationTasks(){

    }

}
