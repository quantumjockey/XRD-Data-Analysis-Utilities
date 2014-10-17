package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteOrder;

public class ShortWrapper extends WrapperBase{

    public ShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    public short get(){
        return buffer.getShort();
    }
}
