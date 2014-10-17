package mvvmbase.window;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class WindowControllerBase implements Initializable {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    protected boolean isActive;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public WindowControllerBase(){
        this.isActive = true;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb){
        performInitializationTasks();
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    protected abstract void performInitializationTasks();

}
