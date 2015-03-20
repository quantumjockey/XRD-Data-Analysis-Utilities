package edu.hipsec.xrddau.app.controls.zoomcontrol;

import com.quantumjockey.melya.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.zoomcontrol.components.ZoomControlController;

public class ZoomControl extends VBoxComplement<ZoomControlController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, ZoomControlController.class);
    }

}
