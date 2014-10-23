package app.multipleimagesubtractor;

import app.multipleimagesubtractor.components.MultipleImageSubtractorController;
import javafx.scene.layout.VBox;
import mvvmbase.markup.initialization.MarkupInitializer;
import mvvmbase.markup.initialization.MarkupInitializerMacro;

public class MultipleImageSubtractor extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "MultipleImageSubtractorView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MultipleImageSubtractor() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MultipleImageSubtractorController getController(){
        return (MultipleImageSubtractorController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, MultipleImageSubtractorController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
