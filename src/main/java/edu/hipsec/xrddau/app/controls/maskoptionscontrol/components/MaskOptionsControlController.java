package edu.hipsec.xrddau.app.controls.maskoptionscontrol.components;

import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import edu.hipsec.xrddau.app.controls.valueadjuster.ValueAdjuster;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class MaskOptionsControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private CheckBox enableStickyBounds;

    @FXML
    private ValueAdjuster maxBound;

    @FXML
    private ValueAdjuster minBound;

    @FXML
    private ColorPicker overlayHue;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<Color> maskHue = new SimpleObjectProperty<>();

    public final Color getMaskHue() {
        return this.maskHue.get();
    }

    public final void setMaskHue(Color maskHue) {
        this.maskHue.set(maskHue);
    }

    public ObjectProperty<Color> maskHueProperty() {
        return this.maskHue;
    }

    private IntegerProperty lowerBound = new SimpleIntegerProperty();

    public final int getLowerBound() {
        return this.lowerBound.get();
    }

    public final void setLowerBound(int lowerBound) {
        this.lowerBound.set(lowerBound);
    }

    public IntegerProperty lowerBoundProperty() {
        return this.lowerBound;
    }

    private BooleanProperty stickyBounds = new SimpleBooleanProperty();

    public final boolean getStickyBounds() {
        return this.stickyBounds.get();
    }

    public final void setStickyBounds(boolean setSticky) {
        this.stickyBounds.set(setSticky);
    }

    public BooleanProperty stickyBoundsProperty() {
        return this.stickyBounds;
    }

    private IntegerProperty upperBound = new SimpleIntegerProperty();

    public final int getUpperBound() {
        return this.upperBound.get();
    }

    public final void setUpperBound(int upperBound) {
        this.upperBound.set(upperBound);
    }

    public IntegerProperty upperBoundProperty() {
        return this.upperBound;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setLimiters(int min, int max) {
        minBound.getController().setLimiters(min, max);
        minBound.getController().setDisplayedValue(min);
        maxBound.getController().setLimiters(min, max);
        maxBound.getController().setDisplayedValue(max);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        stickyBoundsProperty().bindBidirectional(enableStickyBounds.selectedProperty());
        upperBoundProperty().bindBidirectional(maxBound.getController().displayedValueProperty());
        lowerBoundProperty().bindBidirectional(minBound.getController().displayedValueProperty());
        maskHueProperty().bindBidirectional(overlayHue.valueProperty());
    }

    @Override
    protected void setDefaults() {

    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}