package xrdtiffoperations.filehandling.bytewrappers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WrapperBase {

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    protected ByteBuffer buffer;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public WrapperBase(byte[] bytes, ByteOrder order){
        buffer = ByteBuffer.wrap(bytes);
        buffer.order(order);
    }

}
