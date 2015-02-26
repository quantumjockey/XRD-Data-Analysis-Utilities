package edu.hipsec.app.controls.doubleadjuster.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;

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
        if (getDisplayedValue() <= getMaxValue() && getDisplayedValue() > getMinValue()) {
            double result = getDisplayedValue() - increment;
            setDisplayedValue(result);
        }
    }

    @FXML
    public void increment() {
        if (getDisplayedValue() < getMaxValue() && getDisplayedValue() >= getMinValue()) {
            double result = getDisplayedValue() + increment;
            setDisplayedValue(result);
        }
    }

    public void setIncrement(double value) {
        increment = value;
    }

    public void setLimiters(double min, double max) {
        if (min > max) {
            setMaxValue(min);
            setMinValue(max);
            setTickUnits(min);
        } else {
            setMaxValue(max);
            setMinValue(min);
            setTickUnits(max);
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void setTickUnits(double maxVal) {
        int tickUnit = (int) maxVal / 3;
        adjustment.setMajorTickUnit((tickUnit != 0) ? Math.abs(tickUnit) : 1);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        value.textProperty().bindBidirectional(displayedValueProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
        adjustment.maxProperty().bindBidirectional(maxValueProperty());
        adjustment.minProperty().bindBidirectional(minValueProperty());
        adjustment.valueProperty().bindBidirectional(displayedValueProperty());
    }

    @Override
    protected void setDefaults() {
        setLimiters(SLIDER_MIN_DEFAULT, SLIDER_MAX_DEFAULT);
        setDisplayedValue(SLIDER_MIN_DEFAULT);
        adjustment.setShowTickMarks(true);
        adjustment.setShowTickLabels(true);
        increment = INCREMENT_DEFAULT;
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
