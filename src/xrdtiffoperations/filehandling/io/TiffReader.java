package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.bytewrappers.IntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.ShortWrapper;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class TiffReader {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // File Data
    private byte[] fileBytesRaw;

    // Image data
    private WritableMARTiffImage marImageData;
    private boolean fileHasBeenRead;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffReader(Path filePath) throws IOException{
        fileBytesRaw = Files.readAllBytes(filePath);
        marImageData = new WritableMARTiffImage(filePath.getFileName().toString());
        fileHasBeenRead = false;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void readFileData(){
        int lastIfdByte;
        getFileHeader(fileBytesRaw);
        lastIfdByte = getIFDByteGroups(fileBytesRaw, marImageData.getFirstIfdOffset());
        getCalibrationData(lastIfdByte, fileBytesRaw);
        retrieveImageData(retrieveImageStartingByte(), retrieveImageHeight(), retrieveImageWidth());
        fileHasBeenRead = true;
    }

    public MARTiffImage getImageData(){
        if (fileHasBeenRead) {
            return marImageData;
        }
        else {
            return null;
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ByteOrder getByteOrder(String orderId){

        ByteOrder order;

        if(orderId == "II"){
            order = ByteOrder.LITTLE_ENDIAN;
        }
        else if(orderId == "MM") {
            order = ByteOrder.BIG_ENDIAN;
        }
        else{
            order = ByteOrder.nativeOrder();
        }

        return order;
    }

    private void getCalibrationData(int ifdEndByte, byte[] fileBytes){
        byte[] bytes = getCalibrationDataBytes(ifdEndByte, fileBytes);
        marImageData.setCalibration(new CalibrationData(
                ifdEndByte,
                searchDirectoriesForTag(FieldTags.X_RESOLUTION_OFFSET),
                searchDirectoriesForTag(FieldTags.Y_RESOLUTION_OFFSET),
                searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET),
                bytes,
                marImageData.getByteOrder()
        ));
    }

    private byte[] getCalibrationDataBytes(int ifdEndByte, byte[] fileBytes){
        int imageStartByte = searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        int bufferLength = imageStartByte - ifdEndByte;
        byte[] data = new byte[bufferLength];
        for (int i = ifdEndByte; i < imageStartByte; i++){
            data[i - ifdEndByte] = fileBytes[i];
        }
        return data;
    }

    private void getFileHeader(byte[] imageData){

        byte[] _byteOrder = new byte[2];
        byte[] _identifier = new byte[2];
        byte[] _ifdOffset = new byte[4];

        int cursor = 0;

        while (cursor < 8) {
            if (cursor < 2){
                _byteOrder[cursor] = imageData[cursor];
            } else if(cursor >= 2 && cursor < 4){
                _identifier[cursor - 2] = imageData[cursor];
            } else if(cursor >= 4 && cursor <= 8) {
                _ifdOffset[cursor - 4] = imageData[cursor];
            }
            cursor++;
        }

        marImageData.setByteOrder(getByteOrder(new String(_byteOrder)));
        marImageData.setIdentifier((new ShortWrapper(_identifier, marImageData.getByteOrder())).get());
        marImageData.setFirstIfdOffset((new IntWrapper(_ifdOffset, marImageData.getByteOrder())).get());
    }

    private int getIFDByteGroups(byte[] imageData, int firstIfdOffset){

        byte[] _fieldsCount = new byte[2];

        for (int i = 0; i < 2; i++) {
            _fieldsCount[i] = imageData[firstIfdOffset + i];
        }

        int fieldsCount = (new ShortWrapper(_fieldsCount, marImageData.getByteOrder())).get();

        int directoryLength = 2 + (fieldsCount * 12) + 4;

        byte[] directoryBytes = new byte[directoryLength];

        for (int i = 0; i < directoryLength; i++){
            directoryBytes[i] = imageData[firstIfdOffset + i];
        }

        ImageFileDirectory directory = new ImageFileDirectory(directoryBytes, marImageData.getByteOrder());
        marImageData.getIfdListing().add(directory);

        return firstIfdOffset + directoryLength;
    }

    private void retrieveImageData(int startingByte, int imageHeight, int imageWidth){
        short[] linearImageArray = new short[imageHeight * imageWidth];
        byte[] pixelTemp = new byte[2];
        int z = 0;
        for(int i = 0; i < (fileBytesRaw.length - startingByte); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp[0] = fileBytesRaw[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp[1] = fileBytesRaw[startingByte + i];
                linearImageArray[z] = (new ShortWrapper(pixelTemp, marImageData.getByteOrder())).get();
                z++;
            }
        }
        marImageData.initializeIntensityMap(imageHeight, imageWidth);
        for (int y = 0; y < imageHeight; y++){
            for (int x = 0; x < imageWidth; x++){
                marImageData.setIntensityMapCoordinate(y, x, linearImageArray[x + (y * imageHeight)]);
            }
        }
    }

    private int retrieveImageStartingByte(){
        return searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
    }

    private int retrieveImageHeight(){
        return searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
    }

    private int retrieveImageWidth(){
        return searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
    }

    private int searchDirectoriesForTag(int tag){
        int _value = 0;
        for (ImageFileDirectory directory : marImageData.getIfdListing()){
            for (FieldInformation item : directory.getFields()){
                if (item.getTag() == tag){
                    _value = item.getValue();
                }
            }
        }
        return _value;
    }

}
