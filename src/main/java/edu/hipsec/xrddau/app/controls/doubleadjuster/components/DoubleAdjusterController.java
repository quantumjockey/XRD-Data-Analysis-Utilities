package edu.hipsec.xrddau.app.controls.doubleadjuster.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import com.quantumjockey.melya.markup.MarkupControllerBase;

import java.text.NumberFormat;

public class DoubleAdjusterController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double SLIDER_MAX_DEFAULT = 10.0;
    private final double SLIDER_MIN_DEFAULT = 1.0;
    private final double INCREMENT_DEFAULT = 0.1;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private TextField value;

    @FXML
    private Slider adjustment;

    private double increment;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private DoubleProperty displayedValue = new SimpleDoubleProperty();

    public final double getDisplayedValue() {
        return this.displayedValue.get();
    }

    public final void setDisplayedValue(double displayedValue) {
        this.displayedValue.set(displayedValue);
    }

    public DoubleProperty displayedValueProperty() {
        return this.displayedValue;
    }

    private DoubleProperty maxValue = new SimpleDoubleProperty();

    public final double getMaxValue() {
        return this.maxValue.get();
    }

    public final void setMaxValue(double maxValue) {
        this.maxValue.set(maxValue);
    }

    public DoubleProperty maxValueProperty() {
        return this.maxValue;
    }

    private DoubleProperty minValue = new SimpleDoubleProperty();

    public final double getMinValue() {
        return this.minValue.get();
    }

    public final void setMinValue(double minValue) {
        this.minValue.set(minValue);
    }

    public DoubleProperty minValueProperty() {
        return this.minValue;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void decrement() {
        if (this.getDisplayedValue() <= this.getMaxValue() && this.getDisplayedValue() > this.getMinValue()) {
            double result = this.getDisplayedValue() - this.increment;
            this.setDisplayedValue(result);
        }
    }

    @FXML
    public void increment() {
        if (this.getDisplayedValue() < this.getMaxValue() && this.getDisplayedValue() >= this.getMinValue()) {
            double result = getDisplayedValue() + this.increment;
            this.setDisplayedValue(result);
        }
    }

    public void setIncrement(double value) {
        this.increment = value;
    }

    public void setLimiters(double min, double max) {
        if (min > max) {
            this.setMaxValue(min);
            this.setMinValue(max);
            this.setTickUnits(min);
        } else {
            this.setMaxValue(max);
            this.setMinValue(min);
            this.setTickUnits(max);
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void setTickUnits(double maxVal) {
        int tickUnit = (int) maxVal / 3;
        this.adjustment.setMajorTickUnit((tickUnit != 0) ? Math.abs(tickUnit) : 1);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        this.value.textProperty().bindBidirectional(this.displayedValueProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
        this.adjustment.maxProperty().bindBidirectional(this.maxValueProperty());
        this.adjustment.minProperty().bindBidirectional(this.minValueProperty());
        this.adjustment.valueProperty().bindBidirectional(this.displayedValueProperty());
    }

    @Override
    protected void setDefaults() {
        setLimiters(SLIDER_MIN_DEFAULT, SLIDER_MAX_DEFAULT);
        setDisplayedValue(SLIDER_MIN_DEFAULT);
        this.adjustment.setShowTickMarks(true);
        this.adjustment.setShowTickLabels(true);
        this.increment = INCREMENT_DEFAULT;
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
