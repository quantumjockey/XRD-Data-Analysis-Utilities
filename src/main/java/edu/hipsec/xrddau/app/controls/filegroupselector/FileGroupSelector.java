package edu.hipsec.xrddau.app.controls.filegroupselector;

import edu.hipsec.xrddau.app.controls.filegroupselector.components.FileGroupSelectorController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class FileGroupSelector extends VBox {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FileGroupSelector() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public FileGroupSelectorController getController() {
        return (FileGroupSelectorController) this.markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, FileGroupSelectorController.class);
        this.markupContainer.load();
    }

}
