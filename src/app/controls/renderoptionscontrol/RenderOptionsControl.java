package app.controls.renderoptionscontrol;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import app.controls.renderoptionscontrol.components.RenderOptionsControlController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

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
        markupContainer = MarkupInitializerMacro.createInitializer(this, RenderOptionsControlController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
