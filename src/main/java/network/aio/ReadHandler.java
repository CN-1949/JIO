package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CallbackReadHandler handler;

    public ReadHandler(AsynchronousSocketChannel channel, CallbackReadHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    public void completed(Integer i, ByteBuffer buf) {
        if (i > 0) {
            buf.flip();
            if (handler != null) {
                byte[] message = new byte[buf.remaining()];
                buf.get(message);
                handler.completed(new SocketChannel(channel), message);
            }
            ByteBuffer buffer = ByteBuffer.allocate(buf.capacity());
            channel.read(buffer, buffer, this);
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
