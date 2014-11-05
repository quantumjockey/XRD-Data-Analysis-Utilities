package app.workspaces.bulkimagecorrection;

import app.workspaces.bulkimagecorrection.components.BulkImageSubtractorController;
import javafx.scene.layout.VBox;
import mvvmbase.markup.initialization.MarkupInitializer;
import mvvmbase.markup.initialization.MarkupInitializerMacro;

public class BulkImageSubtractor extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "BulkImageSubtractorView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public BulkImageSubtractor() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public BulkImageSubtractorController getController(){
        return (BulkImageSubtractorController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, BulkImageSubtractorController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
