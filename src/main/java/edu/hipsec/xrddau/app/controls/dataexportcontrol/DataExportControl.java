package edu.hipsec.xrddau.app.controls.dataexportcontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.dataexportcontrol.components.DataExportControlController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class DataExportControl extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DataExportControlController getController() {
        return (DataExportControlController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DataExportControlController.class);
    }

}
