package network.aio;

import java.nio.channels.AsynchronousSocketChannel;

public interface DataHandler<T> {

    void completed(AsynchronousSocketChannel result, T t);

    void failed(Throwable exc, T t);
}