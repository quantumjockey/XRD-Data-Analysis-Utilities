package edu.hipsec.xrddau.app.controls.diffractionframepalette;

import edu.hipsec.xrddau.app.controls.diffractionframepalette.components.DiffractionFramePaletteController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import javafx.scene.layout.HBox;

public class DiffractionFramePalette extends HBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "DiffractionFramePaletteView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFramePalette() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFramePaletteController getController() {
        return (DiffractionFramePaletteController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFramePaletteController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
