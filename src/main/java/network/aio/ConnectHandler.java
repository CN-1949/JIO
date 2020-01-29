package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

    private CallbackReadHandler handler;
    private int bufferSize;

    public ConnectHandler(CallbackReadHandler handler, int bufferSize) {
        this.handler = handler;
        this.bufferSize = bufferSize;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        channel.read(buffer, buffer, new ReadHandler(channel, handler));
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
        exc.printStackTrace();
    }
}
