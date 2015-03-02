package edu.hipsec.xrddau.app.workspaces.singleimagecorrection;

import edu.hipsec.xrddau.app.workspaces.singleimagecorrection.components.SingleImageCorrectionController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageCorrection extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SingleImageCorrection() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageCorrectionController getController() {
        return (SingleImageCorrectionController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageCorrectionController.class);
        this.markupContainer.load();
    }

}
