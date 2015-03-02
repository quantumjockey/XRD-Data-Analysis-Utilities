package edu.hipsec.xrddau.app.workspaces.singleimagecorrection;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.singleimagecorrection.components.SingleImageCorrectionController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageCorrection extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageCorrectionController getController() {
        return (SingleImageCorrectionController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageCorrectionController.class);
    }

}
