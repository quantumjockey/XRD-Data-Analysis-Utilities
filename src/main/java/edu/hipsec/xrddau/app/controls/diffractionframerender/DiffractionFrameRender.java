package edu.hipsec.xrddau.app.controls.diffractionframerender;

import com.quantumjockey.mvvmbase.markup.scene.layout.GridPaneComplement;
import edu.hipsec.xrddau.app.controls.diffractionframerender.components.DiffractionFrameRenderController;

public class DiffractionFrameRender extends GridPaneComplement<DiffractionFrameRenderController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DiffractionFrameRenderController.class);
    }

}
