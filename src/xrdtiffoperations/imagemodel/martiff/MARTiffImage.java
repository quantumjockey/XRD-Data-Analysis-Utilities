package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.TiffBase;

public class MARTiffImage extends TiffBase{

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final short INTENSITY_MAXIMUM = 32767;
    private final short INTENSITY_MINIMUM = -32768;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private byte[] excessDataBuffer;
    private short[][] intensityMap;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getExcessDataBuffer(){
        return excessDataBuffer;
    }

    public byte getExcessDataBufferByte(int index){
        return excessDataBuffer[index];
    }

    public short getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Mutators ///////////////////////////////////////////////////////////////////

    public void initializeIntensityMap(int height, int width){
        intensityMap = new short[height][width];
    }

    public void setExcessDataBuffer(byte[] buffer){
        excessDataBuffer = buffer;
    }

    public void setIntensityMapCoordinate(int y, int x, short value){
        intensityMap[y][x] = value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short getMaxValue(){
        short maxVal = INTENSITY_MINIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] > maxVal){
                    maxVal = intensityMap[y][x];
                }
            }
        }
        return maxVal;
    }

    public short getMinValue(){
        short minVal = INTENSITY_MAXIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] < minVal){
                    minVal = intensityMap[y][x];
                }
            }
        }
        return minVal;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

}
