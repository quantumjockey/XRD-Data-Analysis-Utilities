package DialogInitialization;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pathoperations.SystemAttributes;

import java.io.File;


public class FileSaveChooserWrapper {

    private FileChooser chooser;
    private Stage chooserWindow;


    public FileSaveChooserWrapper(String title){
        InitializeChooserComponents();
        InitializeChooserAttributes(title);
    }


    public File GetSaveDirectory(){
        return chooser.showSaveDialog(chooserWindow);
    }

    public void SetInitialFileName(String filename){
        chooser.setInitialFileName(filename);
    }

    private void InitializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new FileChooser();
    }

    private void InitializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}
