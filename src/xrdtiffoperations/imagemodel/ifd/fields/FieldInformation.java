package xrdtiffoperations.imagemodel.ifd.fields;

import xrdtiffoperations.wrappers.bytewrappers.IntWrapper;
import xrdtiffoperations.wrappers.bytewrappers.ShortWrapper;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class FieldInformation {

    // Attributes
    public short tag;
    public short type;
    public int count;
    public int value;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FieldInformation(byte[] data, ByteOrder order){

        byte[] _fieldTag = new byte[2];
        byte[] _fieldType = new byte[2];
        byte[] _typeCount = new byte[4];
        byte[] _fieldValue = new byte[4];

        for (int i = 0; i < 12; i++) {
            if (i < 2){
                _fieldTag[i] = data[i];
            }
            else if(i >= 2 && i < 4){
                _fieldType[i - 2] = data[i];
            }
            else if(i >= 4 && i < 8){
                _typeCount[i - 4] = data[i];
            }
            else if(i >= 8 && i < 12){
                _fieldValue[i - 8] = data[i];
            }
        }

        tag = (new ShortWrapper(_fieldTag, order)).Get();
        type = (new ShortWrapper(_fieldType, order)).Get();
        count = (new ShortWrapper(_typeCount, order)).Get();
        value = (new IntWrapper(_fieldValue, order)).Get();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void Print(){
        System.out.println("------ Field Entry ------");
        System.out.println("Tag: " + tag + " - " + GetTagDescription(tag));
        System.out.println("Type: " + type + " - " + GetFieldType(type));
        System.out.println("Instance Count: " + count);
        System.out.println("Value: " + value);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private String GetFieldType(short _typeId){
        Map<Integer, String> typeIds = new HashMap<>();
        typeIds.put(1, "8-bit unsigned int (byte)");
        typeIds.put(2, "ASCII");
        typeIds.put(3, "16-bit unsigned int (short)");
        typeIds.put(4, "32-bit unsigned int (long)");
        typeIds.put(5, "Rational");
        return (typeIds.containsKey(_typeId)) ? typeIds.get(_typeId) : "(type indeterminable)";
    }

    private String GetTagDescription(short _tagData){
        Map<Integer, String> tags = new HashMap<>();
        tags.put(256, "Image Width");
        tags.put(257, "Image Height");
        tags.put(258, "Bits Per Sample");
        tags.put(259, "Compression");
        tags.put(262, "Photometric Interpretation");
        tags.put(273, "Strip-Offsets (Location of image data in file)");
        tags.put(274, "Orientation");
        tags.put(278, "Rows Per Strip");
        tags.put(279, "Strip Byte Counts (Total number of image data bytes)");
        tags.put(282, "X-Resolution");
        tags.put(283, "Y-Resolution");
        tags.put(296, "Resolution Unit");
        return (tags.containsKey(_tagData)) ? tags.get(_tagData) : "(tag code unrecognized)";
    }
}
