package edu.hipsec.xrddau.app.workspaces.singleimageviewer;

import com.quantumjockey.melya.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.singleimageviewer.components.SingleImageViewerController;

public class SingleImageViewer extends VBoxComplement<SingleImageViewerController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, SingleImageViewerController.class);
    }

}
