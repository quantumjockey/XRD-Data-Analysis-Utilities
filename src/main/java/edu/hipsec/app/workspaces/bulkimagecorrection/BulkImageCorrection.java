package edu.hipsec.app.workspaces.bulkimagecorrection;

import edu.hipsec.app.workspaces.bulkimagecorrection.components.BulkImageCorrectionController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class BulkImageCorrection extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "BulkImageCorrectionView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BulkImageCorrection() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public BulkImageCorrectionController getController() {
        return (BulkImageCorrectionController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, BulkImageCorrectionController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
