package xrdtiffoperations.filehandling.bytewrappers;

import java.nio.ByteOrder;

public class IntWrapper extends WrapperBase{

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public IntWrapper(byte[] bytes, ByteOrder order){
        super(bytes ,order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        return buffer.getInt();
    }

}
