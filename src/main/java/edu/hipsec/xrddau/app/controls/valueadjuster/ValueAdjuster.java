package edu.hipsec.xrddau.app.controls.valueadjuster;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.xrddau.app.controls.valueadjuster.components.ValueAdjusterController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class ValueAdjuster extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ValueAdjuster() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ValueAdjusterController getController() {
        return (ValueAdjusterController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, ValueAdjusterController.class);
        this.markupContainer.load();
    }

}
