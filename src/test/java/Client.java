import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class Client {
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Future<Void> future = client.connect(new InetSocketAddress("127.0.0.1", 8080));
        future.get();

        client.write(ByteBuffer.wrap("Hello".getBytes())).get();
        client.write(ByteBuffer.wrap("Hello".getBytes())).get();
        client.write(ByteBuffer.wrap("Hello".getBytes())).get();
        client.write(ByteBuffer.wrap("Hello".getBytes())).get();
        client.write(ByteBuffer.wrap("Hello".getBytes())).get();
        client.write(ByteBuffer.wrap("Hello".getBytes())).get();

        client.write(ByteBuffer.wrap("hi".getBytes())).get();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buf) {
                System.out.println(new String(buf.array()));
                ByteBuffer buffer1 = ByteBuffer.allocate(buf.capacity());
                client.read(buffer1, buffer1, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });

        Thread.sleep(2000000);
    }
}