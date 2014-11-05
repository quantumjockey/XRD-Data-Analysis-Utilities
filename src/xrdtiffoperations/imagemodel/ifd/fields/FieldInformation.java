package xrdtiffoperations.imagemodel.ifd.fields;

import xrdtiffoperations.filehandling.bytewrappers.IntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.ShortWrapper;
import java.nio.ByteOrder;

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

}
