package app.martiffviewport;

import mvvmbase.markup.initialization.MarkupInitializer;
import app.martiffviewport.components.MARTiffViewportController;
import javafx.scene.control.TitledPane;
import pathoperations.PathWrapper;
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

    public void renderImage(MARTiffImage image) throws IOException{
        getController().renderImage(image);
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException{
        getController().renderImageFromFile(filePath);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = new MarkupInitializer(new MARTiffViewportController(), MARKUP_FILE);
        markupContainer.setRoot(this);
        markupContainer.load();
    }

}
