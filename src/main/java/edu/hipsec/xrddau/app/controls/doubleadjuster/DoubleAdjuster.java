package edu.hipsec.xrddau.app.controls.doubleadjuster;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.doubleadjuster.components.DoubleAdjusterController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class DoubleAdjuster extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DoubleAdjusterController getController() {
        return (DoubleAdjusterController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DoubleAdjusterController.class);
    }

}
