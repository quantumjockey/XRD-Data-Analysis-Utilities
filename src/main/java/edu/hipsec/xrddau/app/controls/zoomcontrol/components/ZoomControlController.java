package edu.hipsec.xrddau.app.controls.zoomcontrol.components;

import edu.hipsec.xrddau.app.controls.doubleadjuster.DoubleAdjuster;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;

public class ZoomControlController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private DoubleAdjuster zoomScale;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private DoubleProperty maxZoom = new SimpleDoubleProperty();

    public final double getMaxZoom() {
        return this.maxZoom.get();
    }

    public final void setMaxZoom(double maxZoom) {
        this.maxZoom.set(maxZoom);
    }

    public DoubleProperty maxZoomProperty() {
        return this.maxZoom;
    }

    private DoubleProperty minZoom = new SimpleDoubleProperty();

    public final double getMinZoom() {
        return this.minZoom.get();
    }

    public final void setMinZoom(double minZoom) {
        this.minZoom.set(minZoom);
    }

    public DoubleProperty minZoomProperty() {
        return this.minZoom;
    }

    private DoubleProperty zoomLevel = new SimpleDoubleProperty();

    public final double getZoomLevel() {
        return this.zoomLevel.get();
    }

    public final void setZoomLevel(double zoomLevel) {
        this.zoomLevel.set(zoomLevel);
    }

    public DoubleProperty zoomLevelProperty() {
        return this.zoomLevel;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setZoomBounds(double min, double max) {
        this.zoomScale.getController().setLimiters(min, max);
    }

    public void setZoomIncrement(double increment) {
        this.zoomScale.getController().setIncrement(increment);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        zoomLevelProperty().bindBidirectional(this.zoomScale.getController().displayedValueProperty());
        maxZoomProperty().bindBidirectional(this.zoomScale.getController().maxValueProperty());
        minZoomProperty().bindBidirectional(this.zoomScale.getController().minValueProperty());
    }

    @Override
    protected void setDefaults() {
        this.setZoomBounds(0.05, 2.0);
        this.setZoomIncrement(0.05);
        this.setZoomLevel(1.0);
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
