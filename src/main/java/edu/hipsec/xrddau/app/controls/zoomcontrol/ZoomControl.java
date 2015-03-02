package edu.hipsec.xrddau.app.controls.zoomcontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.zoomcontrol.components.ZoomControlController;

public class ZoomControl extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ZoomControlController getController() {
        return (ZoomControlController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, ZoomControlController.class);
    }

}
