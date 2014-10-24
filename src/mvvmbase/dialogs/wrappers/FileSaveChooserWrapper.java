package mvvmbase.dialogs.wrappers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import filesystembase.paths.SystemAttributes;
import java.io.File;

public class FileSaveChooserWrapper {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    private FileChooser chooser;
    private Stage chooserWindow;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public FileSaveChooserWrapper(String title){
        initializeChooserComponents();
        initializeChooserAttributes(title);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public File getSaveDirectory(){
        return chooser.showSaveDialog(chooserWindow);
    }

    public void setInitialFileName(String filename){
        chooser.setInitialFileName(filename);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void initializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new FileChooser();
    }

    private void initializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}

