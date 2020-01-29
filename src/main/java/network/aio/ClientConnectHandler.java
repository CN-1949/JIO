package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

    private CallbackReadHandler handler;
    private int bufferSize;

    public ClientConnectHandler(CallbackReadHandler handler, int bufferSize) {
        this.handler = handler;
        this.bufferSize = bufferSize;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        channel.read(buffer, buffer, new ClientReadHandler(channel, handler));
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
        exc.printStackTrace();
    }
}
