package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;

import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private ByteOrder byteOrder;
    private short identifier;
    private String filename;
    private int firstIfdOffset;
    private ArrayList<ImageFileDirectory> ifdListing;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ByteOrder getByteOrder(){
        return byteOrder;
    }

    public short getIdentifier(){
        return identifier;
    }

    public String getFilename(){
        return filename;
    }

    public ArrayList<ImageFileDirectory> getIfdListing(){
        return ifdListing;
    }

    public int getFirstIfdOffset(){
        return firstIfdOffset;
    }

    /////////// Mutators ///////////////////////////////////////////////////////////////////

    public void setByteOrder(ByteOrder order){
        byteOrder = order;
    }

    public void setIdentifier(short id){
        identifier = id;
    }

    public void setFilename(String name){
        filename = name;
    }

    public void setIfdListing(ArrayList<ImageFileDirectory> listing){
        ifdListing = listing;
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
