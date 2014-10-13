package app.martiffviewport;

import MvvmBase.initialization.MarkupInitializer;
import javafx.scene.control.TitledPane;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

import java.io.IOException;

public class MARTiffViewport extends TitledPane {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "MARTiffViewportView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewport() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MARTiffViewportController getController(){
        return (MARTiffViewportController)markupContainer.getController();
    }

    public void RenderImage(MARTiffImage image) throws IOException{
        getController().RenderImage(image);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = new MarkupInitializer(new MARTiffViewportController(), MARKUP_FILE);
        markupContainer.setRoot(this);
        markupContainer.load();
    }

}
