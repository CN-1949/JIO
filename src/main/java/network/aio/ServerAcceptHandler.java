package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private CallbackReadHandler handler;
    private int bufferSize;

    public ServerAcceptHandler(CallbackReadHandler handler, int bufferSize) {
        this.handler = handler;
        this.bufferSize = bufferSize;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel server) {
        server.accept(null, this);
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        channel.read(buffer, buffer, new ServerReadHandler(channel, handler));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel server) {
        exc.printStackTrace();
    }
}
