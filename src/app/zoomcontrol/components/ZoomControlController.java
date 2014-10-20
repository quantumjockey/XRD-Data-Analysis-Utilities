package app.zoomcontrol.components;

import app.doubleadjuster.DoubleAdjuster;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import mvvmbase.markup.MarkupControllerBase;

public class ZoomControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private DoubleAdjuster zoomScale;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private DoubleProperty zoomLevel = new SimpleDoubleProperty();
    public final double getZoomLevel(){ return this.zoomLevel.get(); }
    public final void setZoomLevel(double zoomLevel){ this.zoomLevel.set(zoomLevel); }
    public DoubleProperty zoomLevelProperty(){ return this.zoomLevel; }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setZoomBounds(double min, double max){
        zoomScale.getController().setLimiters(min, max);
    }

    public void setZoomIncrement(double increment){
        zoomScale.getController().setIncrement(increment);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {
       zoomScale = new DoubleAdjuster();
    }

    @Override
    protected void setBindings(){
        zoomLevelProperty().bindBidirectional(zoomScale.getController().displayedValueProperty());
    }

    @Override
    protected void setDefaults(){
        setZoomBounds(0.05, 2.0);
        setZoomIncrement(0.05);
        setZoomLevel(1.0);
    }

    @Override
    protected void setListeners(){

    }

}
