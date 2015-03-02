package edu.hipsec.xrddau.app.controls.diffractionframeworkspace;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.xrddau.app.controls.diffractionframeworkspace.components.DiffractionFrameWorkspaceController;
import javafx.scene.control.TitledPane;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import com.quantumjockey.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import java.io.IOException;

public class DiffractionFrameWorkspace extends TitledPane {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameWorkspace() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFrameWorkspaceController getController() {
        return (DiffractionFrameWorkspaceController) this.markupContainer.getController();
    }

    public void renderImage(DiffractionFrame image) throws IOException {
        this.getController().renderImageData(image);
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        this.getController().renderImageFromFile(filePath);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        this.markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFrameWorkspaceController.class);
        this.markupContainer.load();
    }

}
