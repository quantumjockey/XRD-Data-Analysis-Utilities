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

    private StringProperty operationTag = new SimpleStringProperty();
    public final String getOperationTag(){ return this.operationTag.get(); }
    public final void setOperationTag(String operationTag){ this.operationTag.set(operationTag); }
    public StringProperty operationTagProperty(){ return this.operationTag; }

    private IntegerProperty renderedHeight = new SimpleIntegerProperty();
    public final int getRenderedHeight(){ return this.renderedHeight.get(); }
    public final void setRenderedHeight(int renderedHeight){ this.renderedHeight.set(renderedHeight); }
    public IntegerProperty renderedHeightProperty(){ return this.renderedHeight; }

    private IntegerProperty renderedWidth = new SimpleIntegerProperty();
    public final int getRenderedWidth(){ return this.renderedWidth.get(); }
    public final void setRenderedWidth(int renderedWidth){ this.renderedWidth.set(renderedWidth); }
    public IntegerProperty renderedWidthProperty(){ return this.renderedWidth; }

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
        this.setOperationTag(_operation);
        this.setZoom(_zoom);
        Bindings.bindBidirectional(renderedHeightProperty(),renderedWidthProperty());
        this.setRenderedHeight(_renderedSize);
    }

}
