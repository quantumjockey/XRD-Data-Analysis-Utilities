package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by quantumjockey on 9/17/14.
 */
public class WrapperBase {

    protected ByteBuffer buffer;

    public WrapperBase(byte[] bytes, ByteOrder order){
        buffer = ByteBuffer.wrap(bytes);
        buffer.order(order);
    }
}
