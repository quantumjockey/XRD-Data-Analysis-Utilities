package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteOrder;

public class LongWrapper extends WrapperBase{

    public LongWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    public long Get(){
        return buffer.getLong();
    }
}
