package app.maskoptionscontrol.components;

import mvvmbase.markup.MarkupControllerBase;
import app.valueadjuster.ValueAdjuster;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class MaskOptionsControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ValueAdjuster maxBound;

    @FXML
    private ValueAdjuster minBound;

    @FXML
    private ColorPicker overlayHue;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<Color> maskHue = new SimpleObjectProperty<>();
    public final Color getMaskHue(){ return this.maskHue.get(); }
    public final void setMaskHue(Color maskHue) {
        this.maskHue.set(maskHue);
    }
    public ObjectProperty<Color> maskHueProperty(){ return this.maskHue; }

    private IntegerProperty lowerBound = new SimpleIntegerProperty();
    public final int getLowerBound(){ return this.lowerBound.get(); }
    public final void setLowerBound(int lowerBound){ this.lowerBound.set(lowerBound); }
    public IntegerProperty lowerBoundProperty(){ return this.lowerBound; }

    private IntegerProperty upperBound = new SimpleIntegerProperty();
    public final int getUpperBound(){ return this.upperBound.get(); }
    public final void setUpperBound(int upperBound){ this.upperBound.set(upperBound); }
    public IntegerProperty upperBoundProperty(){ return this.upperBound; }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MaskOptionsControlController(){
        CreateCustomControlInstances();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setLimiters(int min, int max){
        minBound.getController().setLimiters(min, max);
        minBound.getController().setDisplayedValue(min);
        maxBound.getController().setLimiters(min, max);
        maxBound.getController().setDisplayedValue(max);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void CreateCustomControlInstances() {
        maxBound = new ValueAdjuster();
        minBound = new ValueAdjuster();
    }

    @Override
    protected void performInitializationTasks(){
        setBindings();
    }

    private void setBindings(){
        upperBoundProperty().bindBidirectional(maxBound.getController().displayedValueProperty());
        lowerBoundProperty().bindBidirectional(minBound.getController().displayedValueProperty());
        maskHueProperty().bindBidirectional(overlayHue.valueProperty());
    }

}
