package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import filesystembase.bytewrappers.IntWrapper;
import filesystembase.bytewrappers.ShortWrapper;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class ImageFileDirectory {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private static int FIELD_ENTRY_LENGTH = 12;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int numFields;
    public ArrayList<FieldInformation> fields;
    public int nextOffset;
    public int offset;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public ImageFileDirectory(byte[] directoryBytes, int _offset, ByteOrder order){
        offset = _offset;
        numFields = getFieldsCount(directoryBytes, order);
        nextOffset = getNextOffset(directoryBytes, order);
        fields = new ArrayList<>();
        getFields(directoryBytes, order);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public int getTagValue(short specifiedTag){
        int value = -1;
        for (FieldInformation item : fields){
            if (item.tag == specifiedTag) {
                value = item.value;
            }
        }
        return value;
    }

    public void printDirectory(){
        System.out.println("-----------IFD-------------");
        System.out.println("Number of fields: " + numFields);
        System.out.println("Offset: " + offset);
        System.out.println("Next Directory Offset: " + nextOffset + ((nextOffset == 0) ? " (No Additional Directories)" : ""));
        for (FieldInformation item : fields){
            item.print();
        }
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void getFields(byte[] bytes, ByteOrder byteOrder){
        int cursor = 2;
        for (int i = 0; i < numFields; i++){
            byte[] fieldBytes = new byte[FIELD_ENTRY_LENGTH];
            for (int j = 0; j < FIELD_ENTRY_LENGTH; j++){
                fieldBytes[j] = bytes[cursor + j];
            }
            fields.add(new FieldInformation(fieldBytes, byteOrder));
            cursor += FIELD_ENTRY_LENGTH;
        }
    }

    private int getFieldsCount(byte[] bytes, ByteOrder byteOrder){
        byte[] _fieldsCount = new byte[2];
        for (int i = 0; i < 2; i++) {
             _fieldsCount[i] = bytes[i];
        }
        return (new ShortWrapper(_fieldsCount, byteOrder)).get();
    }

    private int getNextOffset(byte[] bytes, ByteOrder byteOrder){
        int cursor = 2 + numFields * FIELD_ENTRY_LENGTH;
        byte[] _nextOffset = new byte[4];
        for (int i = 0; i < 4; i++) {
            _nextOffset[i] = bytes[cursor + i];
        }
        return (new IntWrapper(_nextOffset, byteOrder)).get();
    }

}
