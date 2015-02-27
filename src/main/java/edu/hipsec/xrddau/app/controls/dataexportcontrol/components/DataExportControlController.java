package edu.hipsec.xrddau.app.controls.dataexportcontrol.components;

import com.quantumjockey.dialogs.FileSaveChooserWrapper;
import edu.hipsec.xrddau.app.filesystem.FileSysWriter;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.math.DataMasking;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import com.quantumjockey.mvvmbase.action.ActionDelegate;
import com.quantumjockey.mvvmbase.controls.initialization.ChoiceBoxExt;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;

public class DataExportControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ChoiceBox<String> exportOptions;

    private ArrayList<ActionDelegate<Void>> options;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<ActionDelegate> selected = new SimpleObjectProperty<>();

    public final ActionDelegate getSelected() {
        return this.selected.get();
    }

    public final void setSelected(ActionDelegate selected) {
        this.selected.set(selected);
    }

    public ObjectProperty<ActionDelegate> selectedProperty() {
        return this.selected;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void exportImage() {
        getSelected().invoke();
    }

    public void exportImageWithAttributes(DiffractionFrame image, String imageType, int maskLb, int maskUb) {

        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        boolean isMasked = (image.getMaxValue() != maskUb || image.getMinValue() != maskLb);

        if (isMasked) {
            DataMasking.maskImage(image, maskLb, maskUb);
            dialog.setInitialFileName(image.getIdentifier());
        } else
            dialog.setInitialFileName(image.getIdentifier());

        dialog.setFileType(new FileChooser.ExtensionFilter(imageType, "*.tif"));
        File destination = dialog.getSaveDirectory();
        FileSysWriter.writeImageData(destination, image, imageType);
    }

    public void updateSelections(ArrayList<ActionDelegate<Void>> selections) {
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
    protected void setBindings() {

    }

    @Override
    protected void setDefaults() {
        createSelections();
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {
        ChangeListener<Number> onSelectedChanged = (observable, oldValue, newValue) -> {
            setSelected(options.get(newValue.intValue()));
            exportOptions.setTooltip(new Tooltip(options.get(newValue.intValue()).getIdentifier()));
        };
        exportOptions.getSelectionModel().selectedIndexProperty().addListener(onSelectedChanged);
    }

}
