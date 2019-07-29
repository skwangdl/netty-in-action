package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Listing 2.4 Main class for the client
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start()
        throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();      //创建Bootstrap
            b.group(group)                      //指定EventLoopGroup 以处理客户端事件；需要适用于NIO的实现
                .channel(NioSocketChannel.class)//适用于NIO传输的Channel类型
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception {  //在创建Channel时，向ChannelPipeline中添加一个EchoClientHandler
                        ch.pipeline().addLast(
                             new EchoClientHandler());
                    }
                });
            ChannelFuture f = b.connect().sync();       //连接到远程节点，阻塞等待直到连接完成
            f.channel().closeFuture().sync();           //阻塞直到Channel关闭
        } finally {
            group.shutdownGracefully().sync();          //关闭释放所有资源
        }
    }

    public static void main(String[] args)
            throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("input your host :");
        String host = scanner.nextLine();
        System.out.println("input your port :");
        int port = Integer.valueOf(scanner.nextLine());
        new EchoClient(host, port).start();
    }
}

