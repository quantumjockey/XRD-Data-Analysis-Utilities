package app.controls.valueadjuster;

import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializer;
import app.controls.valueadjuster.components.ValueAdjusterController;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.markup.initialization.MarkupInitializerMacro;

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
        markupContainer = MarkupInitializerMacro.createInitializer(this, ValueAdjusterController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
