package app.valueadjuster;

import MvvmBase.markup.MarkupControllerBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import java.text.NumberFormat;

public class ValueAdjusterController extends MarkupControllerBase {

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
    public void Decrement(){
        if (getDisplayedValue() <= getMaxValue() && getDisplayedValue() > getMinValue()){
            int result = getDisplayedValue() - 1;
            setDisplayedValue(result);
        }
    }

    @FXML
    public void Increment(){
        if (getDisplayedValue() < getMaxValue() && getDisplayedValue() >= getMinValue()){
            int result = getDisplayedValue() + 1;
            setDisplayedValue(result);
        }
    }

    @Override
    protected void performInitializationTasks(){
        setDefaults();
        setBindings();
        setListeners();
    }

    public void setLimiters(int min, int max){
        setMaxValue(max);
        setMinValue(min);
    }

    public void setValue(int value){
        setDisplayedValue(value);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void setBindings(){
        value.textProperty().bindBidirectional(displayedValueProperty(), new NumberStringConverter(NumberFormat.getNumberInstance()));
        adjustment.valueProperty().bindBidirectional(displayedValueProperty());
    }

    private void setDefaults(){
        setMaxValue(0);
        setMinValue(0);
        setDisplayedValue(0);
    }

    private void setListeners(){
        value.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    int temp = Integer.parseInt(newValue);
                    if (temp <= getMaxValue() && temp >= getMinValue()) {
                        value.setText(Integer.toString(temp));
                    } else {
                        if (temp > getMaxValue()) {
                            temp = getMaxValue();
                        }
                        if (temp < getMinValue()) {
                            temp = getMinValue();
                        }
                        value.setText(Integer.toString(temp));
                    }
                } catch (NumberFormatException e) {
                    value.setText(oldValue);
                } catch (NullPointerException e){
                    value.setText(oldValue);
                }
            }
        });
    }

}
