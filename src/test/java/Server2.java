import network.aio.CallbackReadHandler;
import network.aio.ServerBootstrap;
import network.aio.SocketChannel;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Server2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group();
        bootstrap.bind(8080);
        bootstrap.bufferSize(1024);
        bootstrap.readHandler(new CallbackReadHandler() {
            @Override
            public void completed(SocketChannel channel, byte[] bytes) {
                channel.async("Hi".getBytes());
                System.out.println("---------->" + new String(bytes) + "<----------");
            }

            @Override
            public void failed(Throwable exc) {
                exc.printStackTrace();
            }
        });
        AsynchronousServerSocketChannel server = bootstrap.open();
//        Thread.sleep(100000000);
    }
}