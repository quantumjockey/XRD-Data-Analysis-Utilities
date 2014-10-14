package xrdtiffoperations.wrappers.bytewrappers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WrapperBase {

    protected ByteBuffer buffer;

    public WrapperBase(byte[] bytes, ByteOrder order){
        buffer = ByteBuffer.wrap(bytes);
        buffer.order(order);
    }
}
