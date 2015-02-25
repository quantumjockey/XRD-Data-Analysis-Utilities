package app.controls.renderoptionscontrol.components;

import javafx.beans.property.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import com.quantumjockey.mvvmbase.controls.initialization.ChoiceBoxExt;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import com.quantumjockey.colorramps.GradientRamp;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RenderOptionsControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private CheckBox adaptiveRendering;

    @FXML
    private ChoiceBox<String> availableRamps;

    @FXML
    private Label scaleOffsetDisplay;

    private ArrayList<GradientRamp> ramps;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<GradientRamp> activeRamp = new SimpleObjectProperty<>();

    public final GradientRamp getActiveRamp() {
        return this.activeRamp.get();
    }

    public final void setActiveRamp(GradientRamp activeRamp) {
        this.activeRamp.set(activeRamp);
    }

    public ObjectProperty<GradientRamp> activeRampProperty() {
        return this.activeRamp;
    }

    private BooleanProperty adaptiveRender = new SimpleBooleanProperty();

    public final boolean getAdaptiveRender() {
        return this.adaptiveRender.get();
    }

    public final void setAdaptiveRender(boolean checked) {
        this.adaptiveRender.set(checked);
    }

    public BooleanProperty adaptiveRenderProperty() {
        return this.adaptiveRender;
    }

    private IntegerProperty scaleOffset = new SimpleIntegerProperty();

    public final int getScaleOffset() {
        return this.scaleOffset.get();
    }

    public final void setScaleOffset(int scaleOffset) {
        this.scaleOffset.set(scaleOffset);
    }

    public IntegerProperty scaleOffsetProperty() {
        return this.scaleOffset;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setOffset(int value) {
        setScaleOffset(value);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void createRamps() {
        ramps = new ArrayList<>();
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.BLUE, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.BEIGE, Color.BISQUE, Color.ORANGE, Color.MAGENTA, Color.LIGHTPINK, Color.WHITE}, "Fit2D Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}, "Spectrum Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}, "Reduced Spectrum Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.RED, Color.ORANGE, Color.YELLOW}, "Autumn Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED}, "Primaries Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.GREEN, Color.ORANGE}, "Secondaries Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.VIOLET, Color.BLUE, Color.BLACK, Color.YELLOW, Color.WHITE}, "High Contrast Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.WHITE}, "Grayscale Ramp"));
        ramps.add(new GradientRamp(new Color[]{Color.WHITE, Color.BLACK}, "Inverse Grayscale Ramp"));
        setActiveRamp(ramps.get(0));
    }

    private void initializeRamps() {
        ArrayList<String> rampList = new ArrayList<>();
        ramps.forEach((item) -> rampList.add(item.tag));
        ChoiceBoxExt.populate(availableRamps, rampList, null);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        adaptiveRendering.selectedProperty().bindBidirectional(adaptiveRenderProperty());
        scaleOffsetDisplay.textProperty().bindBidirectional(scaleOffsetProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
    }

    @Override
    protected void setDefaults() {
        adaptiveRendering.setSelected(false);
        createRamps();
        initializeRamps();
    }

    @Override
    protected void setListeners() {
        ChangeListener<Number> onSelectedChanged = (observable, oldValue, newValue) -> setActiveRamp(ramps.get(newValue.intValue()));
        availableRamps.getSelectionModel().selectedIndexProperty().addListener(onSelectedChanged);
    }

}