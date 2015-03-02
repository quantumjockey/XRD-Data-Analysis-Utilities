package edu.hipsec.xrddau.app.workspaces.singleimageviewer;

import com.quantumjockey.mvvmbase.markup.scene.layout.VBoxComplement;
import edu.hipsec.xrddau.app.workspaces.singleimageviewer.components.SingleImageViewerController;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

public class SingleImageViewer extends VBoxComplement {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public SingleImageViewerController getController() {
        return (SingleImageViewerController) this.markupContainer.getController();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, SingleImageViewerController.class);
    }

}
