package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TiffWriter {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int HEADER_LENGTH = 8;
    private final int IFD_BUFFER_LENGTH = 4;
    private final int IFD_ENTRY_COUNT_LENGTH = 2;
    private final int IFD_LENGTH = 12;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MARTiffImage cachedData;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffWriter(MARTiffImage dataSource){
        cachedData = dataSource;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void write(String path){
        byte[] allBytes = generateFileBytes();
        Path destination = Paths.get(path);
        try {
            Files.write(destination, allBytes);
        }
        catch (IOException e){
            System.out.println("File could not be written.");
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private byte[] createHeaderBytes(ByteOrder order){
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

    private byte[] createIFDBuffer(ByteOrder order){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_BUFFER_LENGTH);
        bytes.order(order);
        for (int i = 0; i < IFD_BUFFER_LENGTH; i++){
            bytes.put((byte)0);
        }
        return bytes.array();
    }

    private byte[] createIFDBytes(ByteOrder order){
        byte[] count = createIFDEntryCountBytes(order);
        int byteCount = IFD_ENTRY_COUNT_LENGTH + (cachedData.getIfdListing().get(0).getFields().size() * IFD_LENGTH) + IFD_BUFFER_LENGTH;
        ByteBuffer bytes = ByteBuffer.allocate(byteCount);
        bytes.order(order);
        bytes.put(count);
        for (FieldInformation item : cachedData.getIfdListing().get(0).getFields()){
            bytes.put(createIFDEntryBytes(order, item));
        }
        bytes.put(createIFDBuffer(order));
        return bytes.array();
    }

    private byte[] createIFDEntryBytes(ByteOrder order, FieldInformation info){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_LENGTH);
        bytes.order(order);
        bytes.putShort(info.getTag());
        bytes.putShort(info.getType());
        bytes.putInt(info.getCount());
        bytes.putInt(info.getValue());
        return bytes.array();
    }

    private byte[] createIFDEntryCountBytes(ByteOrder order){
        ByteBuffer bytes = ByteBuffer.allocate(IFD_ENTRY_COUNT_LENGTH);
        bytes.order(order);
        bytes.putShort((short)cachedData.getIfdListing().get(0).getFields().size());
        return bytes.array();
    }

    private byte[] createImageBytes(ByteOrder order){
        int numBytes = cachedData.getIfdListing().get(0).getTagValue(FieldTags.STRIP_BYTE_COUNTS);
        ByteBuffer bytes = ByteBuffer.allocate(numBytes);
        bytes.order(order);
        int gridHeight = cachedData.getHeight();
        int gridWidth = cachedData.getWidth();
        for (int y = 0; y < gridHeight; y++){
            for (int x = 0; x < gridWidth; x++){
                bytes.putShort(cachedData.getIntensityMapValue(y, x));
            }
        }
        return bytes.array();
    }

    private byte[] createRegionBeforeImageData(ByteOrder order, int lengthOfHeaderPlusIFD) {
        int imageOffset = cachedData.getIfdListing().get(0).getTagValue(FieldTags.STRIP_OFFSETS);
        int regionLength = imageOffset - lengthOfHeaderPlusIFD;
        ByteBuffer bytes = ByteBuffer.allocate(regionLength);
        bytes.order(order);

        bytes.putInt(cachedData.getCalibration().getDetectorXResolution().getNumerator());
        bytes.putInt(cachedData.getCalibration().getDetectorXResolution().getDenominator());
        bytes.putInt(cachedData.getCalibration().getDetectorYResolution().getNumerator());
        bytes.putInt(cachedData.getCalibration().getDetectorYResolution().getDenominator());

        int emptyRegion = regionLength - (cachedData.getCalibration().getCoreCalibrationBytes().length + 16);
        byte[] emptyBytes = new byte[emptyRegion];
        for (int i = 0; i < emptyRegion; i++){
            emptyBytes[i] = 0;
        }
        bytes.put(emptyBytes);
        bytes.put(cachedData.getCalibration().getCoreCalibrationBytes());
        return bytes.array();
    }

    private byte[] generateFileBytes(){
        ByteOrder order = cachedData.getByteOrder();
        byte[] header = createHeaderBytes(order);
        byte[] ifd = createIFDBytes(order);
        int lengthOfHeadPlusIfd = header.length + ifd.length;
        byte[] region = createRegionBeforeImageData(order, lengthOfHeadPlusIfd);
        byte[] image = createImageBytes(order);
        int byteCount = header.length + ifd.length + region.length + image.length;
        ByteBuffer bytes = ByteBuffer.allocate(byteCount);
        bytes.put(header);
        bytes.put(ifd);
        bytes.put(region);
        bytes.put(image);
        return bytes.array();
    }

}
