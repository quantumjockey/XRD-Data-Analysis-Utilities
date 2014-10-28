package app.workspaces.singleimagesubtractor.components;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import mvvmbase.markup.MarkupControllerBase;

public class SingleImageSubtractorController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ComboBox<String> selectedPath;

    @FXML
    private ComboBox<String> subtractedPath;

    @FXML
    private Label rootPath;

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){

    }

    @Override
    protected void setListeners(){

    }

}
