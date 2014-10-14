package app.valueadjuster;

import MvvmBase.initialization.MarkupInitializer;
import javafx.scene.layout.HBox;

public class ValueAdjuster extends HBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "ValueAdjusterView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ValueAdjuster() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ValueAdjusterController getController(){
        return (ValueAdjusterController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = new MarkupInitializer(new ValueAdjusterController(), MARKUP_FILE);
        markupContainer.setRoot(this);
        markupContainer.load();
    }

}
