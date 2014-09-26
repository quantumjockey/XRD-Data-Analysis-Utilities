package MvvmBase.initialization;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by quantumjockey on 8/21/14.
 */
public class WindowInitializer {

    // Private fields
    private FXMLLoader windowLoader;
    private Parent windowRoot;

    // Class constructor
    public WindowInitializer(String markupFileName, Object controllerInstance, Class callingObject) throws Exception {
        windowLoader = new FXMLLoader();
        windowLoader.setController(controllerInstance);
        windowRoot = windowLoader.load(callingObject.getResource(markupFileName));
    }

    // Public methods
    public Parent GetParent(){
        return windowRoot;
    }

    public Scene GetScene(){
        return new Scene(windowRoot);
    }

}
