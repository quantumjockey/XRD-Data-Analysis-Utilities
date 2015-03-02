package edu.hipsec.xrddau.app.controls.filegroupselector;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.filegroupselector.components.FileGroupSelectorController;

public class FileGroupSelector extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public FileGroupSelectorController getController() {
        return (FileGroupSelectorController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, FileGroupSelectorController.class);
    }

}
