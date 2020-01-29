package network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class SocketChannel {

    private AsynchronousSocketChannel channel;

    public SocketChannel(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void async(byte[] message) {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    public void sync(byte[] message) throws ExecutionException, InterruptedException {
        channel.write(ByteBuffer.wrap(message)).get();
    }
}
