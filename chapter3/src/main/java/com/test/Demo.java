package com.test;

public class Demo {
    public static void main(String[] args){
        /**
         * 1. Channel接口
         *      基本的IO操作，Channel提供API降低直接使用Socket的复杂性
         *      EmbeddedChannel  LocalServerChannel  NioDatagramChannel  NioSctpChannel  NioSocketChannel
         *
         * 2. EventLoop接口
         *      定义了Netty的核心抽象，用于处理连接的生命周期中所发生的事件
         *      （1）一个EventLoopGroup包含一个或多个EventLoop；
         *      （2）一个EventLoop 在它的生命周期内只和一个Thread 绑定；
         *      （3）所有由EventLoop 处理的I/O 事件都将在它专有的Thread 上被处理；
         *      （4）一个Channel 在它的生命周期内只注册于一个EventLoop；
         *      （5）一个EventLoop 可能会被分配给一个或多个Channel
         *
         * 3. ChannelFuture接口
         *      Netty 中所有的I/O 操作都是异步的。因为一个操作可能不会
         *      立即返回，所以我们需要一种用于在之后的某个时间点确定其结果的方法。为此，Netty 提供了
         *      ChannelFuture 接口
         */
    }
}
