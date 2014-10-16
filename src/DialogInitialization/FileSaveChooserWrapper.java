package DialogInitialization;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pathoperations.SystemAttributes;
import java.io.File;

public class FileSaveChooserWrapper {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    private FileChooser chooser;
    private Stage chooserWindow;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public FileSaveChooserWrapper(String title){
        InitializeChooserComponents();
        InitializeChooserAttributes(title);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public File GetSaveDirectory(){
        return chooser.showSaveDialog(chooserWindow);
    }

    public void SetInitialFileName(String filename){
        chooser.setInitialFileName(filename);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void InitializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new FileChooser();
    }

    private void InitializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}

