package app.dataexportcontrol.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import mvvmbase.action.ActionDelegate;
import mvvmbase.markup.MarkupControllerBase;

import java.util.ArrayList;

public class DataExportControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ChoiceBox<String> exportOptions;

    private ArrayList<ActionDelegate> options;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<ActionDelegate> selected = new SimpleObjectProperty<>();
    public final ActionDelegate getSelected(){ return this.selected.get(); }
    public final void setSelected(ActionDelegate selected) {
        this.selected.set(selected);
    }
    public ObjectProperty<ActionDelegate> selectedProperty(){ return this.selected; }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void exportImage(){
        getSelected().invoke();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void createSelections() {
        options = new ArrayList<>();
        options.add(new ActionDelegate("Raw Data", () -> exportRawImage()));
        options.add(new ActionDelegate("Masked Data", () -> exportMaskedImage()));
        setSelected(options.get(0));
    }

    private Void exportMaskedImage(){

        return null;
    }

    private Void exportRawImage(){
        //        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
//        dialog.setInitialFileName(cachedImage.filename);
//        File destination = dialog.getSaveDirectory();
//        if (destination != null) {
//            TiffWriter writer = new TiffWriter(cachedImage);
//            writer.write(destination.getPath());
//        }
        return null;
    }

    private void initializeSelections() {
        ArrayList<String> rampList = new ArrayList<>();
        options.forEach((item) -> rampList.add(item.id));
        exportOptions.getItems().clear();
        exportOptions.setItems(FXCollections.observableList(rampList));
        exportOptions.getSelectionModel().select(0);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings(){

    }

    @Override
    protected void setDefaults(){
        createSelections();
        initializeSelections();
    }

    @Override
    protected void setListeners(){
        ChangeListener<Number> onSelectedChanged = (observable, oldValue, newValue) -> setSelected(options.get(newValue.intValue()));
        exportOptions.getSelectionModel().selectedIndexProperty().addListener(onSelectedChanged);
    }

}
