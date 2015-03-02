package edu.hipsec.xrddau.app.workspaces.singleimageviewer;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.singleimageviewer.components.SingleImageViewerController;

public class SingleImageViewer extends VBoxComplement<SingleImageViewerController> {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, SingleImageViewerController.class);
    }

}
