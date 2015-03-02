package edu.hipsec.xrddau.app.workspaces.bulkimagecorrection;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.bulkimagecorrection.components.BulkImageCorrectionController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class BulkImageCorrection extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public BulkImageCorrectionController getController() {
        return (BulkImageCorrectionController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, BulkImageCorrectionController.class);
    }

}
