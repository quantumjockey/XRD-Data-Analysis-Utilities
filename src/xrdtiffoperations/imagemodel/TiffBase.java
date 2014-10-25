package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // File Header
    private ByteOrder byteOrder;
    private short identifier;
    private int firstIfdOffset;

    // File Meta
    public String filename;

    // IFD Meta
    public ArrayList<ImageFileDirectory> ifdListing;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ByteOrder getByteOrder(){
        return byteOrder;
    }

    public short getIdentifier(){
        return identifier;
    }

    public int getFirstIfdOffset(){
        return firstIfdOffset;
    }

    public void setByteOrder(ByteOrder order){
        byteOrder = order;
    }

    public void setIdentifier(short id){
        identifier = id;
    }

    public void setFirstIfdOffset(int offset){
        firstIfdOffset = offset;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename = _filename;
    }
}
