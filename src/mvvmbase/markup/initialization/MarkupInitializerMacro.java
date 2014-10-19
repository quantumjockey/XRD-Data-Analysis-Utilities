package mvvmbase.markup.initialization;

import javafx.scene.Node;

public class MarkupInitializerMacro {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static MarkupInitializer createInitializer(Node _rootObject, Class _controllerClass, String _markupName){
        MarkupInitializer markupContainer = new MarkupInitializer(_controllerClass, _markupName);
        markupContainer.setRoot(_rootObject);
        return markupContainer;
    }

}
