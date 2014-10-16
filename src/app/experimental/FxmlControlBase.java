package app.experimental;

import MvvmBase.initialization.MarkupInitializer;
import javafx.scene.Node;

public class FxmlControlBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected MarkupInitializer markupContainer;
    protected String markupFile;
    protected Node root;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FxmlControlBase(Node _root, Object _controller, String _markupFile) {
        initializeComponents(_root, _controller, _markupFile);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(Node _rootObject, Object _controller, String _markupName){
        markupContainer = new MarkupInitializer(_controller, _markupName);
        markupContainer.setRoot(_rootObject);
        markupContainer.load();
    }

}
