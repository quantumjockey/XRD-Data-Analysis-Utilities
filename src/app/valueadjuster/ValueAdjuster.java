package app.valueadjuster;

import MvvmBase.initialization.MarkupInitializer;
import app.valueadjuster.components.ValueAdjusterController;
import javafx.scene.layout.VBox;

public class ValueAdjuster extends VBox {

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
