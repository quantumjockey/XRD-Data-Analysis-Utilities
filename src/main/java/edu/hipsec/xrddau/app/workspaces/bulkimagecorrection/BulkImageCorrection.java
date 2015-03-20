package edu.hipsec.xrddau.app.workspaces.bulkimagecorrection;

import com.quantumjockey.melya.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.bulkimagecorrection.components.BulkImageCorrectionController;

public class BulkImageCorrection extends VBoxComplement<BulkImageCorrectionController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, BulkImageCorrectionController.class);
    }

}
