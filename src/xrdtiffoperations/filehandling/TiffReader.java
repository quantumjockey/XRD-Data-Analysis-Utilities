package xrdtiffoperations.filehandling;

import filesystembase.paths.PathWrapper;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import filesystembase.bytewrappers.IntWrapper;
import filesystembase.bytewrappers.ShortWrapper;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class TiffReader {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // File Data
    private byte[] fileBytesRaw;
    private String fullFilePath;

    // Image data
    private MARTiffImage marImageData;
    private boolean fileHasBeenRead;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffReader(String filePath) throws IOException{
        fullFilePath = filePath;
        fileBytesRaw = Files.readAllBytes(FileSystems.getDefault().getPath(filePath));
        marImageData = new MARTiffImage((new PathWrapper(filePath)).getPathTail());
        fileHasBeenRead = false;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void readFileData(boolean printInfoToConsole){
        getFileHeader(fileBytesRaw);
        getIFDByteGroups(fileBytesRaw, marImageData.getFirstIfdOffset());
        retrieveImageData(retrieveImageStartingByte(), retrieveImageHeight(), retrieveImageWidth());
        getExcessDataBytes(fileBytesRaw);
        if (printInfoToConsole) {
            printFileInfo();
        }
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

    private void getExcessDataBytes(byte[] bytes){
        byte[] data = new byte[3930];
        for (int i = 166; i < 1024; i++){
            data[i - 166] = bytes[i];
        }
        marImageData.setExcessDataBuffer(data);
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

    private void getIFDByteGroups(byte[] imageData, int firstIfdOffset){

        int directoryOffset = firstIfdOffset;

        byte[] _fieldsCount = new byte[2];

        for (int i = 0; i < 2; i++) {
            _fieldsCount[i] = imageData[directoryOffset + i];
        }

        int fieldsCount = (new ShortWrapper(_fieldsCount, marImageData.getByteOrder())).get();

        int directoryLength = 2 + (fieldsCount * 12) + 4;

        byte[] directoryBytes = new byte[directoryLength];

        for (int i = 0; i < directoryLength; i++){
            directoryBytes[i] = imageData[directoryOffset + i];
        }

        ImageFileDirectory directory = new ImageFileDirectory(directoryBytes, directoryOffset, marImageData.getByteOrder());
        marImageData.getIfdListing().add(directory);
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

    ////////////////////////////////////////////////////////

    private void printFileInfo(){
        System.out.println("---------- File Info ----------");
        printFileName();
        printTotalBytes(fileBytesRaw);
        PrintFileHeader();
        printIFDMetadata();
    }

    private void printFileName(){
        System.out.println("File name (full path): " + fullFilePath);
    }

    private void PrintFileHeader(){
        System.out.println("Byte Order: " + marImageData.getByteOrder());
        System.out.println("Identifier: " + marImageData.getIdentifier());
        System.out.println("First IFD Offset: " + marImageData.getFirstIfdOffset());
    }

    private void printTotalBytes(byte[] imageData){
        System.out.println("Total number of bytes: " + imageData.length);
    }

    private void printIFDMetadata(){
        for (ImageFileDirectory item : marImageData.getIfdListing()){
            item.printDirectory();
        }
    }

}