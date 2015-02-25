package app.workspaces.singleimagecorrection;

import app.workspaces.singleimagecorrection.components.SingleImageSubtractorController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageSubtractor extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "SingleImageSubtractorView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SingleImageSubtractor() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageSubtractorController getController() {
        return (SingleImageSubtractorController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageSubtractorController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
