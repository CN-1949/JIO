package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private ReadHandler handler;

    public ServerReadHandler(AsynchronousSocketChannel channel, ReadHandler handler) {
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
    public void failed(Throwable exc, ByteBuffer attachment) {
        if (handler == null) {
            exc.printStackTrace();
        } else {
            handler.failed(exc);
        }
    }
}
