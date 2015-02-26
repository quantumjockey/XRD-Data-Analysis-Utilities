package edu.hipsec.app.controls.maskoptionscontrol;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.app.controls.maskoptionscontrol.components.MaskOptionsControlController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class MaskOptionsControl extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "MaskOptionsControlView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MaskOptionsControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MaskOptionsControlController getController() {
        return (MaskOptionsControlController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, MaskOptionsControlController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
