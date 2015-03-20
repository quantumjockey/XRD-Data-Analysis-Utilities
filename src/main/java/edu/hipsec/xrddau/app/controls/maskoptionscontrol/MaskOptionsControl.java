package edu.hipsec.xrddau.app.controls.maskoptionscontrol;

import com.quantumjockey.melya.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.maskoptionscontrol.components.MaskOptionsControlController;

public class MaskOptionsControl extends VBoxComplement<MaskOptionsControlController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, MaskOptionsControlController.class);
    }

}
