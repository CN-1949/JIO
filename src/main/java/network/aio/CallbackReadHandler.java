package network.aio;

import java.nio.channels.AsynchronousSocketChannel;

public interface CallbackReadHandler {

    void completed(AsynchronousSocketChannel channel, byte[] bytes);

    void failed(Throwable exc);
}