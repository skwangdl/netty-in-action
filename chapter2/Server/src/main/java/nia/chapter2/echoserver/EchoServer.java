package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)
        throws Exception {
        System.out.println("please input your server port");
        Scanner scanner = new Scanner(System.in);
        new EchoServer(Integer.valueOf(scanner.nextLine())).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();     //创建EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();      //创建ServerBootstrap
            b.group(group)
                .channel(NioServerSocketChannel.class)      //指定所使用的NIO 传输Channel
                .localAddress(new InetSocketAddress(port))  //使用指定的端口设置套接字地址
                .childHandler(new ChannelInitializer<SocketChannel>() {     //添加一个EchoServerHandler到子Channel的ChannelPipeline
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(serverHandler);               //EchoServerHandler被标注为@Shareable，所以可以总是使用同样的实例
                    }
                });

            ChannelFuture f = b.bind().sync();      //异步绑定服务器，调用sync阻塞直到绑定完成
            System.out.println(EchoServer.class.getName() +
                " started and listening for connections on " + f.channel().localAddress());
            f.channel().closeFuture().sync();       //获取Channel的CloseFuture，并且阻塞当前线程直到完成业务
        } finally {
            group.shutdownGracefully().sync();      //关闭释放所有资源
        }
    }
}
