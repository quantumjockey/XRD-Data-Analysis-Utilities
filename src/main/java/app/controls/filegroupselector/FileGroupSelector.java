package app.controls.filegroupselector;

import app.controls.filegroupselector.components.FileGroupSelectorController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class FileGroupSelector extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "FileGroupSelectorView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FileGroupSelector() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public FileGroupSelectorController getController() {
        return (FileGroupSelectorController) markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, FileGroupSelectorController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
