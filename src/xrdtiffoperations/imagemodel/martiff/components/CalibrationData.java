package xrdtiffoperations.imagemodel.martiff.components;

import xrdtiffoperations.filehandling.bytewrappers.IntWrapper;
import java.nio.ByteOrder;

public class CalibrationData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private byte[] coreCalibrationBytes;
    private ResolutionAxis detectorXResolution;
    private ResolutionAxis detectorYResolution;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getCoreCalibrationBytes(){
        return coreCalibrationBytes;
    }

    public ResolutionAxis getDetectorXResolution(){
        return detectorXResolution;
    }

    public ResolutionAxis getDetectorYResolution(){
        return detectorYResolution;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public CalibrationData(int ifdEndByte, int xResByte, int yResByte, int calibrationStartByte, byte[] dataBuffer, ByteOrder order) {

        int relativeCalibrationOffset, relativeXResOffset, relativeYResOffset;

        relativeXResOffset = xResByte - ifdEndByte;;
        relativeYResOffset = yResByte - ifdEndByte;
        getDetectorResolution(dataBuffer, relativeXResOffset, relativeYResOffset, order);

        relativeCalibrationOffset = calibrationStartByte - ifdEndByte;
        getCoreCalibrationData(dataBuffer, relativeCalibrationOffset, order);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void getCoreCalibrationData(byte[] buffer, int calOffset, ByteOrder order){
        int calibrationLength = buffer.length - calOffset;
        byte[] bytes = new byte[calibrationLength];
        for(int i = 0; i < calibrationLength; i++){
            bytes[i] = buffer[i + calOffset];
        }
        // Encoding for detector outputs required before this can properly read
        // central section reads when UTF-16 charset selected
        coreCalibrationBytes = bytes;
    }

    private void getDetectorResolution(byte[] buffer, int relX, int relY, ByteOrder order){
        detectorXResolution = new ResolutionAxis(
                getResValue(relX, buffer, order),
                getResValue(relX + 4, buffer, order)
        );
        detectorYResolution = new ResolutionAxis(
                getResValue(relY, buffer, order),
                getResValue(relY + 4, buffer, order)
        );
    }

    private int getResValue(int offset, byte[] buffer, ByteOrder order){
        int floatLength = 8;
        byte[] temp = new byte[floatLength];
        for (int i = 0; i < floatLength; i++){
            temp[i] = buffer[offset + i];
        }
        return (new IntWrapper(temp, order)).get();
    }

}
