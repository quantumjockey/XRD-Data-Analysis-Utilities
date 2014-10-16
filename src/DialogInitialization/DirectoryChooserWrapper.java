package DialogInitialization;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pathoperations.SystemAttributes;

import java.io.File;

public class DirectoryChooserWrapper {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    private DirectoryChooser chooser;
    private Stage chooserWindow;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public DirectoryChooserWrapper(String title){
        InitializeChooserComponents();
        InitializeChooserAttributes(title);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public File GetSelectedDirectory(){
        return chooser.showDialog(chooserWindow);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void InitializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new DirectoryChooser();
    }

    private void InitializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}
