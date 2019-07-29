package nia.chapter10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class TestMessageToMessageCodecServer {
    private final int port;

    public TestMessageToMessageCodecServer(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        System.out.println("please input your server port: ");
        Scanner scanner = new Scanner(System.in);
        new TestMessageToMessageCodecServer(Integer.valueOf(scanner.nextLine())).start();
    }

    private void start(){
        final WebSocketConvertHandler webSocketConvertHandler = new WebSocketConvertHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
                .childHandler(new WebSocketConvertHandler());
        ChannelFuture f = null;
        try {
            f = serverBootstrap.bind().sync();
            System.out.println(TestMessageToMessageCodecServer.class.getName() +
                    " started and listening for connections on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
