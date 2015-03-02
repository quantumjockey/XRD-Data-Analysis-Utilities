package edu.hipsec.xrddau.app.controls.renderoptionscontrol;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.xrddau.app.controls.renderoptionscontrol.components.RenderOptionsControlController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class RenderOptionsControl extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public RenderOptionsControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public RenderOptionsControlController getController() {
        return (RenderOptionsControlController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, RenderOptionsControlController.class);
        this.markupContainer.load();
    }

}
