package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase {

    // File Header
    public ByteOrder byteOrder;
    public int identifier;
    public int firstIfdOffset;

    // File Meta
    public String filename;


    // IFD Meta
    public ArrayList<ImageFileDirectory> ifdListing;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename = _filename;
    }
}
