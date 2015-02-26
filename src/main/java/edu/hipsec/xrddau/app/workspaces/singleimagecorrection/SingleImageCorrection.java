package edu.hipsec.xrddau.app.workspaces.singleimagecorrection;

import edu.hipsec.xrddau.app.workspaces.singleimagecorrection.components.SingleImageCorrectionController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageCorrection extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "SingleImageCorrectionView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SingleImageCorrection() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageCorrectionController getController() {
        return (SingleImageCorrectionController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageCorrectionController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
