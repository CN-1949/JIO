package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private AsynchronousServerSocketChannel server;
    private ReadHandler readHandler;
    private int bufferSize;

    public ServerAcceptHandler(AsynchronousServerSocketChannel server, ReadHandler readHandler, int bufferSize) {
        this.server = server;
        this.readHandler = readHandler;
        this.bufferSize = bufferSize;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, Void attachment) {
        server.accept(null, this);
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        channel.read(buffer, buffer, new ServerReadHandler(channel, readHandler));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
    }
}
