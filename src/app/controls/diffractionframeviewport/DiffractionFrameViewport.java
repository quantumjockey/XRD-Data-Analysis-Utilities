package app.controls.diffractionframeviewport;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import app.controls.diffractionframeviewport.components.DiffractionFrameViewportController;
import javafx.scene.control.TitledPane;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;
import com.quantumjockey.paths.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
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

    public void renderImage(MARTiffImage image) throws IOException{
        getController().renderImage(image);
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
