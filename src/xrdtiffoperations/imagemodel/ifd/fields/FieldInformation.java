package xrdtiffoperations.imagemodel.ifd.fields;

import xrdtiffoperations.readers.bytewrappers.IntWrapper;
import xrdtiffoperations.readers.bytewrappers.ShortWrapper;

import java.nio.ByteOrder;

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
        String typeId;
        switch (_typeId){
            case 1:
                typeId = "8-bit unsigned int (byte)";
                break;
            case 2:
                typeId = "ASCII";
                break;
            case 3:
                typeId = "16-bit unsigned int (short)";
                break;
            case 4:
                typeId = "32-bit unsigned int (long)";
                break;
            case 5:
                typeId = "Rational";
                break;
            default:
                typeId = "(type indeterminable)";
        }
        return typeId;
    }

    private String GetTagDescription(short _tagData){
        String tagId;
        switch (_tagData){
            case 256:
                tagId = "Image Width";
                break;
            case 257:
                tagId = "Image Height";
                break;
            case 258:
                tagId = "Bits Per Sample";
                break;
            case 259:
                tagId = "Compression";
                break;
            case 262:
                tagId = "Photometric Interpretation";
                break;
            case 273:
                tagId = "Strip-Offsets (Location of image data in file)";
                break;
            case 274:
                tagId = "Orientation";
                break;
            case 278:
                tagId = "Rows Per Strip";
                break;
            case 279:
                tagId = "Strip Byte Counts (Total number of image data bytes)";
                break;
            case 282:
                tagId = "X-Resolution";
                break;
            case 283:
                tagId = "Y-Resolution";
                break;
            case 296:
                tagId = "Resolution Unit";
                break;
            default:
                tagId = "(tag code unrecognized)";
        }
        return tagId;
    }
}
