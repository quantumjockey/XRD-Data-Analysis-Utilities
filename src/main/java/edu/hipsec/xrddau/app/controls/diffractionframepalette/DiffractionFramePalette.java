package edu.hipsec.xrddau.app.controls.diffractionframepalette;

import com.quantumjockey.melya.markup.scene.layout.HBoxComplement;
import edu.hipsec.xrddau.app.controls.diffractionframepalette.components.DiffractionFramePaletteController;

public class DiffractionFramePalette extends HBoxComplement<DiffractionFramePaletteController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DiffractionFramePaletteController.class);
    }

}
