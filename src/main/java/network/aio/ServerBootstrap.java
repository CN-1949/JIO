package network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ServerBootstrap {

    private int                             port;
    private List<EncoderHandler>            encoders;
    private List<DecoderHandler>            decoders;
    private List<DataHandler>               handlers;
    private Map<SocketOption, Object>       options;
    private AsynchronousChannelGroup        group;
    private AsynchronousServerSocketChannel server;

    public ServerBootstrap() {
        encoders = new LinkedList<>();
        decoders = new LinkedList<>();
        handlers = new LinkedList<>();
        options  = new LinkedHashMap<>();
    }

    public void group() throws IOException {
        int threadSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        ExecutorService group = new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("ServerBootstrap");
                return thread;
            }
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

    public ServerBootstrap handler(DataHandler<?> handler) {
        handlers.add(handler);
        return this;
    }

    public ServerBootstrap handler(DecoderHandler<?> handler) {
        decoders.add(handler);
        return this;
    }

    public ServerBootstrap handler(EncoderHandler handler) {
        encoders.add(handler);
        return this;
    }

    public ServerBootstrap bind(int port) {
        this.port = port;
        return this;
    }

    public ServerBootstrap open() throws IOException, InterruptedException {
        server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress("0.0.0.0", port));

        // help gc
        options.forEach((k, v) -> {
            try {
                server.setOption(k, v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        options.clear();
        options = null;

        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, ByteBuffer>() {
            @Override
            public void completed(AsynchronousSocketChannel result, ByteBuffer attachment) {
                server.accept(null, this);

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });

        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return this;
    }
}