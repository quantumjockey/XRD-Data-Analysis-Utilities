package DialogInitialization;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pathoperations.SystemAttributes;

import java.io.File;

/**
 * Created by quantumjockey on 9/9/14.
 */
public class DirectoryChooserWrapper {

    private DirectoryChooser chooser;
    private Stage chooserWindow;


    public DirectoryChooserWrapper(String title){
        InitializeChooserComponents();
        InitializeChooserAttributes(title);
    }


    public File GetSelectedDirectory(){
        return chooser.showDialog(chooserWindow);
    }

    private void InitializeChooserComponents(){
        chooserWindow = new Stage();
        chooser = new DirectoryChooser();
    }

    private void InitializeChooserAttributes(String chooserTitle){
        chooser.setInitialDirectory(new File(SystemAttributes.USER_HOME));
        chooser.setTitle(chooserTitle);
    }

}
