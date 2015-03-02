package edu.hipsec.xrddau.app.controls.diffractionframepalette;

import com.quantumjockey.mvvmbase.markup.scene.layout.HBoxComplement;
import edu.hipsec.xrddau.app.controls.diffractionframepalette.components.DiffractionFramePaletteController;

public class DiffractionFramePalette extends HBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFramePaletteController getController() {
        return (DiffractionFramePaletteController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DiffractionFramePaletteController.class);
    }

}
