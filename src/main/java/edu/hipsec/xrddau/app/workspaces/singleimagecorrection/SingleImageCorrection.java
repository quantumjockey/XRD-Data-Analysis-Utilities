package edu.hipsec.xrddau.app.workspaces.singleimagecorrection;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.singleimagecorrection.components.SingleImageCorrectionController;

public class SingleImageCorrection extends VBoxComplement<SingleImageCorrectionController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, SingleImageCorrectionController.class);
    }

}
