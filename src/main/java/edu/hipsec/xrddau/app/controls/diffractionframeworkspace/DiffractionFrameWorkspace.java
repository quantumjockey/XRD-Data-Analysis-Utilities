package edu.hipsec.xrddau.app.controls.diffractionframeworkspace;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import edu.hipsec.xrddau.app.controls.diffractionframeworkspace.components.DiffractionFrameWorkspaceController;
import javafx.scene.control.TitledPane;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import com.quantumjockey.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import java.io.IOException;

public class DiffractionFrameWorkspace extends TitledPane {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "DiffractionFrameWorkspaceView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameWorkspace() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFrameWorkspaceController getController() {
        return (DiffractionFrameWorkspaceController) markupContainer.getController();
    }

    public void renderImage(DiffractionFrame image) throws IOException {
        getController().renderImageOnLoad(image);
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        getController().renderImageFromFile(filePath);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents() {
        markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFrameWorkspaceController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
