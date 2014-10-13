package app.martiffviewport;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

public class MARTiffViewportModel {

    /////////// Properties //////////////////////////////////////////////////////////////////

    private ObjectProperty<MARTiffImage> cachedImage = new SimpleObjectProperty<>();
    public final MARTiffImage getCachedImage(){ return this.cachedImage.get(); }
    public final void setCachedImage(MARTiffImage cachedImage){ this.cachedImage.set(cachedImage); }
    public ObjectProperty<MARTiffImage> cachedImageProperty(){ return this.cachedImage; }

    private StringProperty filename = new SimpleStringProperty();
    public final String getFilename(){ return this.filename.get(); }
    public final void setFilename(String filename){ this.filename.set(filename); }
    public StringProperty filenameProperty(){ return this.filename; }

    private IntegerProperty viewportHeight = new SimpleIntegerProperty();
    public final int getViewportHeight(){ return this.viewportHeight.get(); }
    public final void setViewportHeight(int viewportHeight){ this.viewportHeight.set(viewportHeight); }
    public IntegerProperty viewportHeightProperty(){ return this.viewportHeight; }

    private StringProperty viewportMode = new SimpleStringProperty();
    public final String getViewportMode(){ return this.viewportMode.get(); }
    public final void setViewportMode(String viewportMode){ this.viewportMode.set(viewportMode); }
    public StringProperty viewportModeProperty(){ return this.viewportMode; }

    private IntegerProperty viewportWidth = new SimpleIntegerProperty();
    public final int getViewportWidth(){ return this.viewportWidth.get(); }
    public final void setViewportWidth(int viewportWidth){ this.viewportWidth.set(viewportWidth); }
    public IntegerProperty viewportWidthProperty(){ return this.viewportWidth; }

    private DoubleProperty zoom = new SimpleDoubleProperty();
    public final double getZoom(){ return this.zoom.get(); }
    public final void setZoom(double zoom){ this.zoom.set(zoom); }
    public DoubleProperty zoomProperty(){ return this.zoom; }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportModel(MARTiffImage _image, int _renderedSize){
        this(_image, ViewportModes.VIEWER_OPERATION, _renderedSize, 1.0);
    }

    public MARTiffViewportModel(MARTiffImage _image, String _operation, int _renderedSize, double _zoom){
        this.setCachedImage(_image);
        this.setFilename(_image.filename);
        this.setViewportMode(_operation);
        this.setZoom(_zoom);
        Bindings.bindBidirectional(viewportHeight, viewportWidth);
        this.setViewportHeight(_renderedSize);
    }

}
