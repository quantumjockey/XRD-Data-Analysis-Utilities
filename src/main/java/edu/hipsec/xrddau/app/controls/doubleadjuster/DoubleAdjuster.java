package edu.hipsec.xrddau.app.controls.doubleadjuster;

import edu.hipsec.xrddau.app.controls.doubleadjuster.components.DoubleAdjusterController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class DoubleAdjuster extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "DoubleAdjusterView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DoubleAdjuster() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DoubleAdjusterController getController() {
        return (DoubleAdjusterController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DoubleAdjusterController.class, MARKUP_FILE);
        this.markupContainer.load();
    }

}
