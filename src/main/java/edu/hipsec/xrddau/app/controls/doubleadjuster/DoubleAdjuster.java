package edu.hipsec.xrddau.app.controls.doubleadjuster;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.doubleadjuster.components.DoubleAdjusterController;

public class DoubleAdjuster extends VBoxComplement<DoubleAdjusterController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DoubleAdjusterController.class);
    }

}
