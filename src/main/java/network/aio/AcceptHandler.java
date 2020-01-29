package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private CallbackReadHandler handler;
    private int bufferSize;

    public AcceptHandler(CallbackReadHandler handler, int bufferSize) {
        this.handler = handler;
        this.bufferSize = bufferSize;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel server) {
        server.accept(server, this);
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        channel.read(buffer, buffer, new ReadHandler(channel, handler));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel server) {
        exc.printStackTrace();
    }
}
