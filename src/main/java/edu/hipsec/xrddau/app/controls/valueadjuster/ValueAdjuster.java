package edu.hipsec.xrddau.app.controls.valueadjuster;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.valueadjuster.components.ValueAdjusterController;

public class ValueAdjuster extends VBoxComplement<ValueAdjusterController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, ValueAdjusterController.class);
    }

}
