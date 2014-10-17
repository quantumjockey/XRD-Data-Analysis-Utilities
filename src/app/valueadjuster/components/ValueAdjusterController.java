package app.valueadjuster.components;

import mvvmbase.markup.MarkupControllerBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import java.text.NumberFormat;

public class ValueAdjusterController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double SLIDER_MAX = 100.0;
    private final double SLIDER_MIN = 0.0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private TextField value;

    @FXML
    private Slider adjustment;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private IntegerProperty displayedValue = new SimpleIntegerProperty();
    public final int getDisplayedValue(){ return this.displayedValue.get(); }
    public final void setDisplayedValue(int displayedValue) {
        this.displayedValue.set(displayedValue);
    }
    public IntegerProperty displayedValueProperty(){ return this.displayedValue; }

    private IntegerProperty maxValue = new SimpleIntegerProperty();
    public final int getMaxValue(){ return this.maxValue.get(); }
    public final void setMaxValue(int maxValue){ this.maxValue.set(maxValue); }
    public IntegerProperty maxValueProperty(){ return this.maxValue; }

    private IntegerProperty minValue = new SimpleIntegerProperty();
    public final int getMinValue(){ return this.minValue.get(); }
    public final void setMinValue(int minValue){ this.minValue.set(minValue); }
    public IntegerProperty minValueProperty(){ return this.minValue; }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ValueAdjusterController(){

    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @FXML
    public void decrement(){
        if (getDisplayedValue() <= getMaxValue() && getDisplayedValue() > getMinValue()){
            int result = getDisplayedValue() - 1;
            setDisplayedValue(result);
        }
    }

    @FXML
    public void increment(){
        if (getDisplayedValue() < getMaxValue() && getDisplayedValue() >= getMinValue()){
            int result = getDisplayedValue() + 1;
            setDisplayedValue(result);
        }
    }

    public void setLimiters(int min, int max){
        if (min > max){
            setMaxValue(min);
            setMinValue(max);
            setTickUnits(min);
        }
        else {
            setMaxValue(max);
            setMinValue(min);
            setTickUnits(max);
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks(){
        setDefaults();
        setBindings();
    }

    private void setBindings(){
        value.textProperty().bindBidirectional(displayedValueProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
        adjustment.maxProperty().bindBidirectional(maxValueProperty());
        adjustment.minProperty().bindBidirectional(minValueProperty());
        adjustment.valueProperty().bindBidirectional(displayedValueProperty());
    }

    private void setDefaults(){
        setLimiters(0, 5000);
        setDisplayedValue(0);
        adjustment.setShowTickMarks(true);
        adjustment.setShowTickLabels(true);
    }

    private void setTickUnits(int maxVal){
        int tickUnit = maxVal / 3;
        adjustment.setMajorTickUnit(tickUnit);
    }

}
