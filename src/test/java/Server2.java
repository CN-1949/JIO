import network.aio.ReadHandler;
import network.aio.ServerAcceptHandler;
import network.aio.ServerBootstrap;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Server2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group();
        bootstrap.bind(8080);
        bootstrap.bufferSize(1024);
        bootstrap.readHandler(new ReadHandler() {
            @Override
            public void completed(AsynchronousSocketChannel channel, byte[] bytes) {
                System.out.println(new String(bytes));
            }

            @Override
            public void failed(Throwable exc) {

            }
        });
        AsynchronousServerSocketChannel server = bootstrap.open();
        Thread.sleep(100000000);
    }
}