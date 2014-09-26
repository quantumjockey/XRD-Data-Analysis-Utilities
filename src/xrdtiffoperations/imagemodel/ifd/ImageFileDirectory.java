package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.readers.bytewrappers.IntWrapper;
import xrdtiffoperations.readers.bytewrappers.ShortWrapper;

import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class ImageFileDirectory {

    // Constants
    int fieldEntryLength = 12;

    // Attributes
    private int numFields;

    // Public members
    public ArrayList<FieldInformation> fields;
    public int nextOffset;
    public int offset;

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public ImageFileDirectory(byte[] directoryBytes, int _offset, ByteOrder order){
        offset = _offset;
        numFields = GetFieldsCount(directoryBytes, order);
        nextOffset = GetNextOffset(directoryBytes, order);
        fields = new ArrayList<FieldInformation>();
        GetFields(directoryBytes, order);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public int GetTagValue(short specifiedTag){
        int value = -1;
        for (FieldInformation item : fields){
            if (item.tag == specifiedTag) {
                value = item.value;
            }
        }
        return value;
    }

    public void PrintDirectory(){
        System.out.println("-----------IFD-------------");
        System.out.println("Number of fields: " + numFields);
        System.out.println("Offset: " + offset);
        System.out.println("Next Directory Offset: " + nextOffset + ((nextOffset == 0) ? " (No Additional Directories)" : ""));
        for (FieldInformation item : fields){
            item.Print();
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void GetFields(byte[] bytes, ByteOrder byteOrder){
        int cursor = 2;
        for (int i = 0; i < numFields; i++){
            byte[] fieldBytes = new byte[fieldEntryLength];
            for (int j = 0; j < fieldEntryLength; j++){
                fieldBytes[j] = bytes[cursor + j];
            }
            fields.add(new FieldInformation(fieldBytes, byteOrder));
            cursor += fieldEntryLength;
        }
    }

    private int GetFieldsCount(byte[] bytes, ByteOrder byteOrder){
        byte[] _fieldsCount = new byte[2];
        for (int i = 0; i < 2; i++) {
             _fieldsCount[i] = bytes[i];
        }
        return (new ShortWrapper(_fieldsCount, byteOrder)).Get();
    }

    private int GetNextOffset(byte[] bytes, ByteOrder byteOrder){
        int cursor = 2 + numFields * fieldEntryLength;
        byte[] _nextOffset = new byte[4];
        for (int i = 0; i < 4; i++) {
            _nextOffset[i] = bytes[cursor + i];
        }
        return (new IntWrapper(_nextOffset, byteOrder)).Get();
    }

}
