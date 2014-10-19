package mvvmbase.markup.initialization;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MarkupInitializer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private FXMLLoader fxmlLoader;
    private URL location;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MarkupInitializer(Class _controllerClass, String markupFile) {
        location = _controllerClass.getResource(markupFile);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void load(){
        try {
            fxmlLoader.load(location.openStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getController(){
        return fxmlLoader.getController();
    }

    public void setRoot(Node rootObject){
        fxmlLoader.setRoot(rootObject);
    }

}
