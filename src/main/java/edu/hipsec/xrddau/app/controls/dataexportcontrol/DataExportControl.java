package edu.hipsec.xrddau.app.controls.dataexportcontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.dataexportcontrol.components.DataExportControlController;

public class DataExportControl extends VBoxComplement<DataExportControlController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DataExportControlController.class);
    }

}
