package app.controls.diffractionframeviewport;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import app.controls.diffractionframeviewport.components.DiffractionFrameViewportController;
import javafx.scene.control.TitledPane;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import com.quantumjockey.paths.PathWrapper;
import xrdtiffoperations.data.DiffractionFrame;
import java.io.IOException;

public class DiffractionFrameViewport extends TitledPane {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "DiffractionFrameViewportView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameViewport() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DiffractionFrameViewportController getController(){
        return (DiffractionFrameViewportController)markupContainer.getController();
    }

    public void renderImage(DiffractionFrame image) throws IOException{
        getController().renderImageOnLoad(image);
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException{
        getController().renderImageFromFile(filePath);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, DiffractionFrameViewportController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
