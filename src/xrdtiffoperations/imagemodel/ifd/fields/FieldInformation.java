package xrdtiffoperations.imagemodel.ifd.fields;

import filesystembase.bytewrappers.IntWrapper;
import filesystembase.bytewrappers.ShortWrapper;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class FieldInformation {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private short tag;
    private short type;
    private int count;
    private int value;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public short getTag(){
        return tag;
    }

    public short getType(){
        return type;
    }

    public int getCount(){
        return count;
    }

    public int getValue(){
        return value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FieldInformation(byte[] fieldData, ByteOrder order){

        byte[] _fieldTag = new byte[2];
        byte[] _fieldType = new byte[2];
        byte[] _typeCount = new byte[4];
        byte[] _fieldValue = new byte[4];

        for (int i = 0; i < 12; i++) {
            if (i < 2){
                _fieldTag[i] = fieldData[i];
            }
            else if(i >= 2 && i < 4){
                _fieldType[i - 2] = fieldData[i];
            }
            else if(i >= 4 && i < 8){
                _typeCount[i - 4] = fieldData[i];
            }
            else if(i >= 8 && i < 12){
                _fieldValue[i - 8] = fieldData[i];
            }
        }

        tag = (new ShortWrapper(_fieldTag, order)).get();
        type = (new ShortWrapper(_fieldType, order)).get();
        count = (new ShortWrapper(_typeCount, order)).get();
        value = (new IntWrapper(_fieldValue, order)).get();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void print(){
        System.out.println("------ Field Entry ------");
        System.out.println("Tag: " + tag + " - " + getTagDescription(tag));
        System.out.println("Type: " + type + " - " + getFieldType(type));
        System.out.println("Instance Count: " + count);
        System.out.println("Value: " + value);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private String getFieldType(short _typeId){
        Short value = _typeId;
        Map<Short, String> typeIds = new HashMap<>();
        typeIds.put(FieldTypes.EIGHT_BIT_UNSIGNED_INT, "8-bit unsigned int (byte)");
        typeIds.put(FieldTypes.ASCII, "ASCII");
        typeIds.put(FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, "16-bit unsigned int (short)");
        typeIds.put(FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, "32-bit unsigned int (long)");
        typeIds.put(FieldTypes.RATIONAL, "Rational");
        return (typeIds.containsKey(value)) ? typeIds.get(value) : "(type indeterminable)";
    }

    private String getTagDescription(short _tagData){
        Short value = _tagData;
        Map<Short, String> tags = new HashMap<>();
        tags.put(FieldTags.IMAGE_WIDTH, "Image Width");
        tags.put(FieldTags.IMAGE_HEIGHT, "Image Height");
        tags.put(FieldTags.BITS_PER_SAMPLE, "Bits Per Sample");
        tags.put(FieldTags.COMPRESSION, "Compression");
        tags.put(FieldTags.PHOTOMETRIC_INTERPRETATION, "Photometric Interpretation");
        tags.put(FieldTags.STRIP_OFFSETS, "Strip-Offsets (Location of image data in file)");
        tags.put(FieldTags.ORIENTATION, "Orientation");
        tags.put(FieldTags.ROWS_PER_STRIP, "Rows Per Strip");
        tags.put(FieldTags.STRIP_BYTE_COUNTS, "Strip Byte Counts (Total number of image data bytes)");
        tags.put(FieldTags.X_RESOLUTION_OFFSET, "X-Resolution Offset");
        tags.put(FieldTags.Y_RESOLUTION_OFFSET, "Y-Resolution Offset");
        tags.put(FieldTags.RESOLUTION_UNIT, "Resolution Unit");
        tags.put(FieldTags.CALIBRATION_DATA_OFFSET, "Calibration Data Offset");
        return (tags.containsKey(value)) ? tags.get(value) : "(tag code unrecognized)";
    }
}
