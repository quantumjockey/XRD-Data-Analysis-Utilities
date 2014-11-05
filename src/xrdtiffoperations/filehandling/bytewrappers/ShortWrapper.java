package xrdtiffoperations.filehandling.bytewrappers;

import java.nio.ByteOrder;

public class ShortWrapper extends WrapperBase{

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short get(){
        return buffer.getShort();
    }

}
