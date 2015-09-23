package edu.hipsec.xrddau.app.controls.diffractionframeworkspace;

import com.quantumjockey.melya.markup.scene.control.TitledPaneComplement;
import edu.hipsec.xrddau.app.controls.diffractionframeworkspace.components.DiffractionFrameWorkspaceController;
import com.quantumjockey.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import java.io.IOException;

public class DiffractionFrameWorkspace extends TitledPaneComplement<DiffractionFrameWorkspaceController> {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void renderImage(DiffractionFrame image) throws IOException {
        this.getController().renderImageData(image);
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        this.getController().renderImageFromFile(filePath);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeComponents() {
        this.intializeMarkup(this, DiffractionFrameWorkspaceController.class);
    }

}
