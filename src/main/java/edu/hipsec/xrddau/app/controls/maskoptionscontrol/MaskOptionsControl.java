package edu.hipsec.xrddau.app.controls.maskoptionscontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.maskoptionscontrol.components.MaskOptionsControlController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class MaskOptionsControl extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MaskOptionsControlController getController() {
        return (MaskOptionsControlController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, MaskOptionsControlController.class);
    }

}
