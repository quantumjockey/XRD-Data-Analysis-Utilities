package app.renderoptionscontrol.components;

import mvvmbase.markup.MarkupControllerBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import xrdtiffvisualization.colorramps.GradientRamp;
import java.util.ArrayList;

public class RenderOptionsControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ChoiceBox<String> availableRamps;

    private ArrayList<GradientRamp> ramps;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<GradientRamp> activeRamp = new SimpleObjectProperty<>();
    public final GradientRamp getActiveRamp(){ return this.activeRamp.get(); }
    public final void setActiveRamp(GradientRamp activeRamp) {
        this.activeRamp.set(activeRamp);
    }
    public ObjectProperty<GradientRamp> activeRampProperty(){ return this.activeRamp; }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public RenderOptionsControlController(){
        CreateCustomControlInstances();
        CreateRamps();
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void CreateCustomControlInstances() {

    }

    private void CreateRamps() {
        ramps = new ArrayList<>();
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}, "Spectrum Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.RED, Color.ORANGE, Color.YELLOW}, "Autumn Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.YELLOW, Color.WHITE}, "High Contrast Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.WHITE}, "Grayscale Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.WHITE, Color.BLACK}, "Inverse Grayscale Ramp"));
        setActiveRamp(ramps.get(0));
    }

    @Override
    protected void performInitializationTasks(){
        InitializeRamps();
        setBindings();
    }

    private void setBindings(){

    }

    private void InitializeRamps() {
        ArrayList<String> rampList = new ArrayList<>();
        for (GradientRamp item : ramps){
            rampList.add(item.tag);
        }
        availableRamps.getItems().clear();
        availableRamps.setItems(FXCollections.observableList(rampList));
        availableRamps.getSelectionModel().select(0);
        availableRamps.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number new_value) {
                setActiveRamp(ramps.get(new_value.intValue()));
            }
        });

    }

}
