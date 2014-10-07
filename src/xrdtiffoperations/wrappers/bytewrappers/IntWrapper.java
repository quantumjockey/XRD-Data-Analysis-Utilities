package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteOrder;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class IntWrapper extends WrapperBase{

    public IntWrapper(byte[] bytes, ByteOrder order){
        super(bytes ,order);
    }

    public int Get(){
        return buffer.getInt();
    }
}
