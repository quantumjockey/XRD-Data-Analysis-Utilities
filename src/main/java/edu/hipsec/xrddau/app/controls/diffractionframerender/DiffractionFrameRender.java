package edu.hipsec.xrddau.app.controls.diffractionframerender;

import com.quantumjockey.mvvmbase.markup.scene.layout.GridPaneComplement;
import edu.hipsec.xrddau.app.controls.diffractionframerender.components.DiffractionFrameRenderController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class DiffractionFrameRender extends GridPaneComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFrameRenderController getController() {
        return (DiffractionFrameRenderController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFrameRenderController.class);
    }

}
