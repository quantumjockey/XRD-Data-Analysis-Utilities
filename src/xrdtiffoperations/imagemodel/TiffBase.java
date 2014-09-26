package xrdtiffoperations.imagemodel;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;

import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by quantumjockey on 9/18/14.
 */
public class TiffBase {

    // File Header
    public ByteOrder byteOrder;
    public int identifier;
    public int firstIfdOffset;

    // File Meta
    protected StringProperty filename = new SimpleStringProperty();
    public final String getFilename() {
        return filename.get();
    }
    public final void setFilename(String value){
        filename.set(value);
    }
    public StringProperty filenameProperty() {
        return filename;
    }


    // IFD Meta
    public ArrayList<ImageFileDirectory> ifdListing;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename.set(_filename);
    }
}
