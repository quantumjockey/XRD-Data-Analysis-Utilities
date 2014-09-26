package xrdtiffoperations.readers.bytewrappers;

import java.nio.ByteOrder;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class ShortWrapper extends WrapperBase{

    public ShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    public short Get(){
        return buffer.getShort();
    }
}
