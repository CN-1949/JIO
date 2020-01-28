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

        Thread.sleep(2000000);
    }
}