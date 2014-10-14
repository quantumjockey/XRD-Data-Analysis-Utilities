package MvvmBase.markup;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class MarkupControllerBase implements Initializable {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb){
        performInitializationTasks();
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    protected abstract void performInitializationTasks();

}
