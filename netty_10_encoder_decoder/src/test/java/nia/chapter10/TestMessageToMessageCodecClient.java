package nia.chapter10;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class TestMessageToMessageCodecClient {
    private final String host;
    private final int port;

    public TestMessageToMessageCodecClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("input your host :");
        String host = scanner.nextLine();
        System.out.println("input your port :");
        int port = Integer.valueOf(scanner.nextLine());
        new TestMessageToMessageCodecClient(host, port).start();
    }

    private void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .handler(new WebSocketConvertHandler());
        try {
            ChannelFuture f = bootstrap.connect().sync();
            f.channel().closeFuture().sync();
            eventLoopGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
