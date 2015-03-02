package edu.hipsec.xrddau.app.controls.diffractionframerender;

import edu.hipsec.xrddau.app.controls.diffractionframerender.components.DiffractionFrameRenderController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import javafx.scene.layout.GridPane;

public class DiffractionFrameRender extends GridPane {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameRender() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFrameRenderController getController() {
        return (DiffractionFrameRenderController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFrameRenderController.class);
        this.markupContainer.load();
    }

}
