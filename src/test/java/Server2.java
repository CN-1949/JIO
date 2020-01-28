import network.aio.ServerBootstrap;

import java.io.IOException;

public class Server2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group();
        bootstrap.bind(8080);
        bootstrap.open();
    }
}