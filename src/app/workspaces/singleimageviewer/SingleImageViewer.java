package app.workspaces.singleimageviewer;

import app.workspaces.singleimageviewer.components.SingleImageViewerController;
import javafx.scene.layout.VBox;
import mvvmbase.markup.initialization.MarkupInitializer;
import mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageViewer extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "SingleImageViewerView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SingleImageViewer() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageViewerController getController(){
        return (SingleImageViewerController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageViewerController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
