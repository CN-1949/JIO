package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CallbackReadHandler handler;

    public ClientReadHandler(AsynchronousSocketChannel channel, CallbackReadHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    public void completed(Integer i, ByteBuffer buf) {
        if (i > 0) {
            if (handler != null) {
                handler.completed(channel, buf.array());
            }
            buf.flip();
            buf.clear();
            channel.read(buf, buf, this);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buf) {
        if (handler != null) {
            handler.failed(exc);
        } else {
            exc.printStackTrace();
        } 
    }
}
