package xrdtiffoperations.readers.bytewrappers;

import java.nio.ByteOrder;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class LongWrapper extends WrapperBase{

    public LongWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    public long Get(){
        return buffer.getLong();
    }
}
