package xrdtiffoperations.readers.filewrappers;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.readers.bytewrappers.IntWrapper;
import xrdtiffoperations.readers.bytewrappers.ShortWrapper;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by quantumjockey on 9/15/14.
 */
public class TiffReader {

    // File Data
    private byte[] fileBytesRaw;
    private String fileName;

    // Image data
    private MARTiffImage marImageData;
    private boolean fileHasBeenRead;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffReader(String filePath) throws IOException{
        fileName = filePath;
        fileBytesRaw = Files.readAllBytes(FileSystems.getDefault().getPath(filePath));
        marImageData = new MARTiffImage(filePath);
        fileHasBeenRead = false;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void ReadFileData(boolean printInfoToConsole){
        GetFileHeader(fileBytesRaw);
        GetIFDByteGroups(fileBytesRaw, marImageData.firstIfdOffset);
        GetImageData(RetrieveImageStartingByte(), RetrieveImageHeight(), RetrieveImageWidth());
        if (printInfoToConsole) {
            PrintFileInfo();
        }
        fileHasBeenRead = true;
    }

    public MARTiffImage GetImageData(){
        if (fileHasBeenRead) {
            return marImageData;
        }
        else {
            return null;
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private ByteOrder GetByteOrder(String orderId){

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

    private void GetFileHeader(byte[] imageData){

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

        marImageData.byteOrder = GetByteOrder(new String(_byteOrder));
        marImageData.identifier = (new ShortWrapper(_identifier, marImageData.byteOrder)).Get();
        marImageData.firstIfdOffset = (new IntWrapper(_ifdOffset, marImageData.byteOrder)).Get();
    }

    private void GetIFDByteGroups(byte[] imageData, int firstIfdOffset){

        int directoryOffset = firstIfdOffset;

        byte[] _fieldsCount = new byte[2];

        for (int i = 0; i < 2; i++) {
            _fieldsCount[i] = imageData[directoryOffset + i];
        }

        int fieldsCount = (new ShortWrapper(_fieldsCount, marImageData.byteOrder)).Get();

        int directoryLength = 2 + (fieldsCount * 12) + 4;

        byte[] directoryBytes = new byte[directoryLength];

        for (int i = 0; i < directoryLength; i++){
            directoryBytes[i] = imageData[directoryOffset + i];
        }

        ImageFileDirectory directory = new ImageFileDirectory(directoryBytes, directoryOffset, marImageData.byteOrder);
        marImageData.ifdListing.add(directory);
    }

    private void GetImageData(int startingByte, int imageHeight, int imageWidth){
        short[] linearImageArray = new short[imageHeight * imageWidth];
        byte[] pixelTemp = new byte[2];
        int z = 0;
        for(int i = 0; i < (fileBytesRaw.length - startingByte); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp[0] = fileBytesRaw[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp[1] = fileBytesRaw[startingByte + i];
                linearImageArray[z] = (new ShortWrapper(pixelTemp, marImageData.byteOrder)).Get();
                z++;
            }
        }
        marImageData.intensityMap = new short[imageHeight][imageWidth];
        for (int i = 0; i < imageHeight; i++){
            for (int j = 0; j < imageWidth; j++){
                marImageData.intensityMap[i][j] = linearImageArray[j + (i * imageHeight)];
            }
        }
    }

    private int RetrieveImageStartingByte(){
        return SearchDirectoriesForTag(273);
    }

    private int RetrieveImageHeight(){
        return SearchDirectoriesForTag(257);
    }

    private int RetrieveImageWidth(){
        return SearchDirectoriesForTag(256);
    }

    private int SearchDirectoriesForTag(int tag){
        int _value = 0;
        for (ImageFileDirectory directory : marImageData.ifdListing){
            for (FieldInformation item : directory.fields){
                if (item.tag == tag){
                    _value = item.value;
                }
            }
        }
        return _value;
    }

    ////////////////////////////////////////////////////////

    private void PrintFileInfo(){
        System.out.println("---------- File Info ----------");
        PrintFileName();
        PrintTotalBytes(fileBytesRaw);
        PrintFileHeader();
        PrintIFDMetadata();
    }

    private void PrintFileName(){
        System.out.println("File name (full path): " + fileName);
    }

    private void PrintFileHeader(){
        System.out.println("Byte Order: " + marImageData.byteOrder);
        System.out.println("Identifier: " + marImageData.identifier);
        System.out.println("First IFD Offset: " + marImageData.firstIfdOffset);
    }

    private void PrintTotalBytes(byte[] imageData){
        System.out.println("Total number of bytes: " + imageData.length);
    }

    private void PrintIFDMetadata(){
        for (ImageFileDirectory item : marImageData.ifdListing){
            item.PrintDirectory();
        }
    }

}
