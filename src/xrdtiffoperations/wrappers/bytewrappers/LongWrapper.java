package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteOrder;

public class LongWrapper extends WrapperBase{

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public LongWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public long get(){
        return buffer.getLong();
    }

}
