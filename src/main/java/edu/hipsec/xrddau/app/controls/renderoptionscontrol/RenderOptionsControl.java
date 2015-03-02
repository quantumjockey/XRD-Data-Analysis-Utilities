package edu.hipsec.xrddau.app.controls.renderoptionscontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.renderoptionscontrol.components.RenderOptionsControlController;

public class RenderOptionsControl extends VBoxComplement<RenderOptionsControlController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, RenderOptionsControlController.class);
    }

}
