package network.aio;

import java.nio.ByteBuffer;

public interface DecoderHandler<T> {
    void decoder(T in, ByteBuffer out);
}