package edu.hipsec.xrddau.app.controls.maskoptionscontrol;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.xrddau.app.controls.maskoptionscontrol.components.MaskOptionsControlController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class MaskOptionsControl extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MaskOptionsControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MaskOptionsControlController getController() {
        return (MaskOptionsControlController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, MaskOptionsControlController.class);
        this.markupContainer.load();
    }

}
