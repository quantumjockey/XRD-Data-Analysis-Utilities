package app.mainwindow;

import app.workspaces.bulkimagecorrection.BulkImageSubtractor;
import app.workspaces.singleimagecorrection.SingleImageSubtractor;
import app.workspaces.singleimageviewer.SingleImageViewer;
import dialogs.DirectoryChooserWrapper;
import mvvmbase.window.WindowControllerBase;
import javafx.fxml.FXML;
import paths.PathWrapper;
import paths.filters.FilterWrapper;

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

    private ArrayList<PathWrapper> availableFiles;
    private File selectedDirectory;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @FXML
    public void exitApplication(){
        System.exit(0);
    }

    @FXML
    public void getDirectoryToDisplay(){
        DirectoryChooserWrapper dialog = new DirectoryChooserWrapper("Select Directory for Images");
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
        FilterWrapper tiffFilter = new FilterWrapper(new String[]{".tif", ".tiff"});
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
