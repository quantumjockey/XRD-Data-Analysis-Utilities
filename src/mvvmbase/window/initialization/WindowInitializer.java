package mvvmbase.window.initialization;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class WindowInitializer {

    /////////// Fields ///////////////////////////////////////////////////////////////////////

    private FXMLLoader windowLoader;
    private Parent windowRoot;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public WindowInitializer(String markupFileName, Object controllerInstance, Class callingObject) throws Exception {
        windowLoader = new FXMLLoader();
        windowLoader.setController(controllerInstance);
        windowRoot = windowLoader.load(callingObject.getResource(markupFileName));
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public Parent getParent(){
        return windowRoot;
    }

    public Scene getScene(){
        return new Scene(windowRoot);
    }

}
