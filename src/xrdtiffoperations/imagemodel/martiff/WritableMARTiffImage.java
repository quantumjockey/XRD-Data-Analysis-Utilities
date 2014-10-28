package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class WritableMARTiffImage extends MARTiffImage{

    /////////// Mutators ///////////////////////////////////////////////////////////////////

    // TiffBase fields

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

    // MARTiffImage fields

    public void initializeIntensityMap(int height, int width){
        intensityMap = new short[height][width];
    }

    public void setCalibration(CalibrationData data){
        calibration = data;
    }

    public void setIntensityMapCoordinate(int y, int x, short value){
        intensityMap[y][x] = value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public WritableMARTiffImage(String _filename){
        super(_filename);
    }

}
