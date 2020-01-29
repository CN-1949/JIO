package network.aio;

public interface CallbackReadHandler {

    void completed(SocketChannel channel, byte[] bytes);

    void failed(Throwable exc);
}