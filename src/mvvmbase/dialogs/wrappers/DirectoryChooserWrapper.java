package mvvmbase.dialogs.wrappers;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import filesystembase.paths.SystemAttributes;

import java.io.File;

public class DirectoryChooserWrapper {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    private DirectoryChooser chooser;
    private Stage chooserWindow;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public DirectoryChooserWrapper(String title){
        initializeChooserComponents();
        initializeChooserAttributes(title);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public File getSelectedDirectory(){
        return chooser.showDialog(chooserWindow);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void initializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new DirectoryChooser();
    }

    private void initializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}
