package network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientBootstrap {

    private Map<SocketOption, Object> options;
    private AsynchronousChannelGroup group;
    private AsynchronousSocketChannel client;
    private CallbackReadHandler handler;
    private String host;
    private int port;
    private int bufferSize;

    public ClientBootstrap() {
        options = new LinkedHashMap<>();
    }

    public ClientBootstrap group() throws IOException {
        int threadSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        ExecutorService group = new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("ServerBootstrap");
            return thread;
        });
        return group(AsynchronousChannelGroup.withThreadPool(group));
    }

    public ClientBootstrap group(ExecutorService group) throws IOException {
        return group(AsynchronousChannelGroup.withThreadPool(group));
    }

    public ClientBootstrap group(AsynchronousChannelGroup group) {
        this.group = group;
        return this;
    }

    public <T> ClientBootstrap option(SocketOption<T> name, T value) {
        if (name != null) {
            if (value == null) {
                options.remove(name);
            } else {
                options.put(name, value);
            }
        }
        return this;
    }

    public ClientBootstrap readHandler(CallbackReadHandler handler) {
        this.handler = handler;
        return this;
    }

    public ClientBootstrap host(String host) {
        this.host = host;
        return this;
    }

    public ClientBootstrap bind(int port) {
        this.port = port;
        return this;
    }

    public ClientBootstrap bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public AsynchronousSocketChannel open() throws IOException, InterruptedException {

        client = AsynchronousSocketChannel.open(group);

        options.forEach((k, v) -> {
            try {
                client.setOption(k, v);
            } catch (IOException ignored) {
            }
        });
        // help gc
        options.clear();
        options = null;

        client.connect(new InetSocketAddress(host, port), client, new ClientConnectHandler(handler, bufferSize));

        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return client;
    }

    public void shutdown() {
        group.shutdown();
    }
}