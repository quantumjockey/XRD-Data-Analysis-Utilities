package edu.hipsec.xrddau.app.workspaces.bulkimagecorrection;

import edu.hipsec.xrddau.app.workspaces.bulkimagecorrection.components.BulkImageCorrectionController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class BulkImageCorrection extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BulkImageCorrection() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public BulkImageCorrectionController getController() {
        return (BulkImageCorrectionController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, BulkImageCorrectionController.class);
        this.markupContainer.load();
    }

}
