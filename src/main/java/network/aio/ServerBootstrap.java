package network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ServerBootstrap {

    private Map<SocketOption, Object> options;
    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel server;
    private ReadHandler readHandler;
    private int port;
    private int bufferSize;

    public ServerBootstrap() {
        options = new LinkedHashMap<>();
    }

    public void group() throws IOException {
        int threadSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        ExecutorService group = new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("ServerBootstrap");
            return thread;
        });
        group(AsynchronousChannelGroup.withThreadPool(group));
    }

    public ServerBootstrap group(ExecutorService group) throws IOException {
        return group(AsynchronousChannelGroup.withThreadPool(group));
    }

    public ServerBootstrap group(AsynchronousChannelGroup group) {
        this.group = group;
        return this;
    }

    public <T> ServerBootstrap option(SocketOption<T> name, T value) {
        if (name != null) {
            if (value == null) {
                options.remove(name);
            } else {
                options.put(name, value);
            }
        }
        return this;
    }

    public ServerBootstrap readHandler(ReadHandler readHandler) {
        this.readHandler = readHandler;
        return this;
    }

    public ServerBootstrap bind(int port) {
        this.port = port;
        return this;
    }

    public ServerBootstrap bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public AsynchronousServerSocketChannel open() throws IOException, InterruptedException {
        server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(port));

        // help gc
        options.forEach((k, v) -> {
            try {
                server.setOption(k, v);
            } catch (IOException ignored) {
            }
        });
        options.clear();
        options = null;

        server.accept(null, new ServerAcceptHandler(server, readHandler, bufferSize));
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return server;
    }

    public void shutdown() {
        group.shutdown();
    }
}