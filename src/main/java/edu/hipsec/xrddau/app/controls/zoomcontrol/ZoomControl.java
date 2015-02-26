package edu.hipsec.xrddau.app.controls.zoomcontrol;

import edu.hipsec.xrddau.app.controls.zoomcontrol.components.ZoomControlController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class ZoomControl extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "ZoomControlView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ZoomControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ZoomControlController getController() {
        return (ZoomControlController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, ZoomControlController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
