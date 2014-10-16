package MvvmBase.initialization;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;

public class MarkupInitializer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private Object controller;
    private FXMLLoader fxmlLoader;
    private URL location;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MarkupInitializer(Object _controller, String markupFile) {
        controller = _controller;
        location = controller.getClass().getResource(markupFile);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void load(){
        try {
            fxmlLoader.load(location.openStream());
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getController(){
        return controller;
    }

    public void setRoot(Node rootObject){
        fxmlLoader.setRoot(rootObject);
    }

}
