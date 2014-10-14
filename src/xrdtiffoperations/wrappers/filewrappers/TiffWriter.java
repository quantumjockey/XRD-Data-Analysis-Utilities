package xrdtiffoperations.wrappers.filewrappers;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TiffWriter {

    // Constants
    private final int HEADER_LENGTH = 8;
    private final int IFD_BUFFER_LENGTH = 4;
    private final int IFD_ENTRY_COUNT_LENGTH = 2;
    private final int IFD_LENGTH = 12;

    // File Data
    private MARTiffImage cachedData;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffWriter(MARTiffImage dataSource){
        cachedData = dataSource;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void Write(String path){
        byte[] allBytes = GenerateFileBytes();
        Path destination = Paths.get(path);
        try {
            Files.write(destination, allBytes);
        }
        catch (IOException e){
            System.out.println("File could not be written.");
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private byte[] CreateHeaderBytes(ByteOrder order){
        ByteBuffer bytes = ByteBuffer.allocate(HEADER_LENGTH);
        bytes.order(order);
        String id;
        if(order == ByteOrder.BIG_ENDIAN){
            id = "MM";
        }
        else{
            id = "II";
        }
        bytes.put(id.getBytes());
        bytes.putShort((short)42);
        bytes.putInt(HEADER_LENGTH);
        return bytes.array();
    }

    private byte[] CreateIFDBuffer(ByteOrder order){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_BUFFER_LENGTH);
        bytes.order(order);
        for (int i = 0; i < IFD_BUFFER_LENGTH; i++){
            bytes.put((byte)0);
        }
        return bytes.array();
    }

    private byte[] CreateIFDBytes(ByteOrder order){
        byte[] count = CreateIFDEntryCountBytes(order);
        int byteCount = IFD_ENTRY_COUNT_LENGTH + (cachedData.ifdListing.get(0).fields.size() * IFD_LENGTH) + IFD_BUFFER_LENGTH;
        ByteBuffer bytes = ByteBuffer.allocate(byteCount);
        bytes.order(order);
        bytes.put(count);
        for (FieldInformation item : cachedData.ifdListing.get(0).fields){
            bytes.put(CreateIFDEntryBytes(order, item));
        }
        bytes.put(CreateIFDBuffer(order));
        return bytes.array();
    }

    private byte[] CreateIFDEntryBytes(ByteOrder order, FieldInformation info){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_LENGTH);
        bytes.order(order);
        bytes.putShort(info.tag);
        bytes.putShort(info.type);
        bytes.putInt(info.count);
        bytes.putInt(info.value);
        return bytes.array();
    }

    private byte[] CreateIFDEntryCountBytes(ByteOrder order){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_ENTRY_COUNT_LENGTH);
        bytes.order(order);
        bytes.putShort((short)cachedData.ifdListing.get(0).fields.size());
        return bytes.array();
    }

    private byte[] CreateImageBytes(ByteOrder order){
        int numBytes = cachedData.ifdListing.get(0).GetTagValue((short)279);
        ByteBuffer bytes = ByteBuffer.allocate(numBytes);
        bytes.order(order);
        int gridHeight = cachedData.intensityMap.length;
        int gridWidth = cachedData.intensityMap[0].length;
        for (int i = 0; i < gridHeight; i++){
            for (int j = 0; j < gridWidth; j++){
                bytes.putShort(cachedData.intensityMap[i][j]);
            }
        }
        return bytes.array();
    }

    private byte[] CreateRegionBeforeImageData(ByteOrder order, int lengthOfHeaderPlusIFD) {
        int imageOffset = cachedData.ifdListing.get(0).GetTagValue((short)273);
        int regionLength = imageOffset - lengthOfHeaderPlusIFD;
        ByteBuffer bytes = ByteBuffer.allocate(regionLength);
        bytes.order(order);
        for (int i = 0; i < regionLength; i++){
            bytes.put((byte)0);
        }
        return bytes.array();
    }

    private byte[] GenerateFileBytes(){
        ByteOrder order = cachedData.byteOrder;
        byte[] header = CreateHeaderBytes(order);
        byte[] ifd = CreateIFDBytes(order);
        int lengthOfHeadPlusIfd = header.length + ifd.length;
        byte[] region = CreateRegionBeforeImageData(order, lengthOfHeadPlusIfd);
        byte[] image = CreateImageBytes(order);
        int byteCount = header.length + ifd.length + region.length + image.length;
        ByteBuffer bytes = ByteBuffer.allocate(byteCount);
        bytes.put(header);
        bytes.put(ifd);
        bytes.put(region);
        bytes.put(image);
        return bytes.array();
    }

}
