package network.aio;

import java.nio.ByteBuffer;
import java.util.List;

public interface EncoderHandler {
    void encoder(ByteBuffer in, List<Object> out);
}