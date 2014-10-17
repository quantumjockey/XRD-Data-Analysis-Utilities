package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteOrder;

public class IntWrapper extends WrapperBase{

    public IntWrapper(byte[] bytes, ByteOrder order){
        super(bytes ,order);
    }

    public int get(){
        return buffer.getInt();
    }
}
