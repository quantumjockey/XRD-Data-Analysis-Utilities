package edu.hipsec.xrddau.app.controls.renderoptionscontrol;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.renderoptionscontrol.components.RenderOptionsControlController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class RenderOptionsControl extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public RenderOptionsControlController getController() {
        return (RenderOptionsControlController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, RenderOptionsControlController.class);
    }

}
