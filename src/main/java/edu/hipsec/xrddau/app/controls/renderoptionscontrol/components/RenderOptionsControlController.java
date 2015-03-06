package edu.hipsec.xrddau.app.controls.renderoptionscontrol.components;

import edu.hipsec.xrdtiffvisualization.ImageTypes;
import javafx.beans.property.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;
import com.quantumjockey.mvvmbase.controls.initialization.ChoiceBoxInitializer;
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
    private HBox adaptiveRenderControl;

    @FXML
    private CheckBox adaptiveRendering;

    @FXML
    private ChoiceBox<String> availableRamps;

    @FXML
    private ChoiceBox<String> availableImageTypes;

    @FXML
    private Label scaleOffsetDisplay;

    private ArrayList<String> imageTypes;
    private ArrayList<GradientRamp> ramps;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private StringProperty activeImageType = new SimpleStringProperty();

    public final String getActiveImageType() {
        return this.activeImageType.get();
    }

    public final void setActiveImageType(String activeImageType) {
        this.activeImageType.set(activeImageType);
    }

    public StringProperty activeImageTypeProperty() {
        return this.activeImageType;
    }

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
        this.setScaleOffset(value);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void createImageTypes() {
        this.imageTypes = new ArrayList<>();
        this.imageTypes.add(ImageTypes.FALSE_COLOR_MAPPING);
        this.imageTypes.add(ImageTypes.GRADIENT_MAPPING);
        this.setActiveImageType(imageTypes.get(0));
    }

    private void createRamps() {
        this.ramps = new ArrayList<>();
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.BLUE, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.BEIGE, Color.BISQUE, Color.ORANGE, Color.MAGENTA, Color.LIGHTPINK, Color.WHITE}, "Fit2D Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}, "Spectrum Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}, "Reduced Spectrum Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.RED, Color.ORANGE, Color.YELLOW}, "Autumn Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED}, "Primaries Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.VIOLET, Color.GREEN, Color.ORANGE}, "Secondaries Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.VIOLET, Color.BLUE, Color.BLACK, Color.YELLOW, Color.WHITE}, "High Contrast Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.BLACK, Color.WHITE}, "Grayscale Ramp"));
        this.ramps.add(new GradientRamp(new Color[]{Color.WHITE, Color.BLACK}, "Inverse Grayscale Ramp"));
        setActiveRamp(ramps.get(0));
    }

    private void initializeImageTypes() {
        ArrayList<String> imageTypeList = new ArrayList<>();
        this.imageTypes.forEach(imageTypeList::add);
        ChoiceBoxInitializer<String> init = new ChoiceBoxInitializer<>(this.availableImageTypes);
        init.populate(imageTypeList, null);
    }

    private void initializeRamps() {
        ArrayList<String> rampList = new ArrayList<>();
        this.ramps.forEach((item) -> rampList.add(item.tag));
        ChoiceBoxInitializer<String> init = new ChoiceBoxInitializer<>(this.availableRamps);
        init.populate(rampList, null);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        this.adaptiveRendering.selectedProperty().bindBidirectional(this.adaptiveRenderProperty());
        this.scaleOffsetDisplay.textProperty().bindBidirectional(this.scaleOffsetProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
    }

    @Override
    protected void setDefaults() {
        this.adaptiveRendering.setSelected(false);
        this.createImageTypes();
        this.createRamps();
        this.initializeImageTypes();
        this.initializeRamps();
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {
        ChangeListener<Number> onSelectedImageTypeChanged = (observable, oldValue, newValue) -> {
            setActiveImageType(this.imageTypes.get(newValue.intValue()));
            if (this.getActiveImageType().equals(ImageTypes.GRADIENT_MAPPING))
                this.adaptiveRenderControl.setDisable(true);
            else
                this.adaptiveRenderControl.setDisable(false);
        };
        ChangeListener<Number> onSelectedRampChanged = (observable, oldValue, newValue) -> setActiveRamp(this.ramps.get(newValue.intValue()));
        this.availableImageTypes.getSelectionModel().selectedIndexProperty().addListener(onSelectedImageTypeChanged);
        this.availableRamps.getSelectionModel().selectedIndexProperty().addListener(onSelectedRampChanged);
    }

}
