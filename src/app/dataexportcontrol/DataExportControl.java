package app.dataexportcontrol;

import app.dataexportcontrol.components.DataExportControlController;
import javafx.scene.layout.VBox;
import mvvmbase.markup.initialization.MarkupInitializer;
import mvvmbase.markup.initialization.MarkupInitializerMacro;

public class DataExportControl extends VBox {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String MARKUP_FILE = "DataExportControlView.fxml";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MarkupInitializer markupContainer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DataExportControl() {
        initializeComponents();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public DataExportControlController getController(){
        return (DataExportControlController)markupContainer.getController();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void initializeComponents(){
        markupContainer = MarkupInitializerMacro.createInitializer(this, DataExportControlController.class, MARKUP_FILE);
        markupContainer.load();
    }

}
