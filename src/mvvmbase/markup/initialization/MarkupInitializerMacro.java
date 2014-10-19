package mvvmbase.markup.initialization;

import javafx.scene.Node;

public class MarkupInitializerMacro {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static MarkupInitializer createInitializer(Node _rootObject, Object _controller, String _markupName){
        MarkupInitializer markupContainer = new MarkupInitializer(_controller, _markupName);
        markupContainer.setRoot(_rootObject);
        return markupContainer;
    }

}
