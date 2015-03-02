package edu.hipsec.xrddau.app.controls.filegroupselector;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.controls.filegroupselector.components.FileGroupSelectorController;

public class FileGroupSelector extends VBoxComplement<FileGroupSelectorController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, FileGroupSelectorController.class);
    }

}
