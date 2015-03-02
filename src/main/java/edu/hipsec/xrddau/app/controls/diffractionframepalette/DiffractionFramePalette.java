package edu.hipsec.xrddau.app.controls.diffractionframepalette;

import edu.hipsec.xrddau.app.controls.diffractionframepalette.components.DiffractionFramePaletteController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import javafx.scene.layout.HBox;

public class DiffractionFramePalette extends HBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFramePalette() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFramePaletteController getController() {
        return (DiffractionFramePaletteController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFramePaletteController.class);
        this.markupContainer.load();
    }

}
