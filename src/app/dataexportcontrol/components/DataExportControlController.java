package app.dataexportcontrol.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import mvvmbase.action.ActionDelegate;
import mvvmbase.controls.ChoiceBoxExt;
import mvvmbase.markup.MarkupControllerBase;

import java.util.ArrayList;

public class DataExportControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ChoiceBox<String> exportOptions;

    private ArrayList<ActionDelegate<Void>> options;

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

    public void updateSelections(ArrayList<ActionDelegate<Void>> selections){
        ArrayList<String> temp = new ArrayList<>();
        options.clear();
        selections.forEach((item) -> {
            temp.add(item.getIdentifier());
            options.add(item);
        });
        ChoiceBoxExt.populate(exportOptions, temp, null);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void createSelections() {
        options = new ArrayList<>();
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
    }

    @Override
    protected void setListeners(){
        ChangeListener<Number> onSelectedChanged = (observable, oldValue, newValue) -> setSelected(options.get(newValue.intValue()));
        exportOptions.getSelectionModel().selectedIndexProperty().addListener(onSelectedChanged);
    }

}
