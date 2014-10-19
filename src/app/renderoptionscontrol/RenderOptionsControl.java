package app.renderoptionscontrol;

import mvvmbase.markup.initialization.MarkupInitializer;
import app.renderoptionscontrol.components.RenderOptionsControlController;
import javafx.scene.layout.VBox;
import mvvmbase.markup.initialization.MarkupInitializerMacro;

public class RenderOptionsControl extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "RenderOptionsControlView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public RenderOptionsControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public RenderOptionsControlController getController(){
        return (RenderOptionsControlController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, new RenderOptionsControlController(), MARKUP_FILE);
        markupContainer.load();
    }

}
