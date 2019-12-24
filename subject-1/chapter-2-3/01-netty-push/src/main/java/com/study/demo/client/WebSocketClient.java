package com.study.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

public final class WebSocketClient {

    public static void main(String[] args) throws Exception {
        final String host = System.getProperty("netease.pushserver.host", "127.0.0.1");
        final String maxSize = System.getProperty("netease.client.port.maxSize", "100");
        final String maxConnections = System.getProperty("netease.client.port.maxConnections", "60000");
        int port = 9001;

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new HttpClientCodec());
                    p.addLast(new HttpObjectAggregator(8192));
                    p.addLast(WebSocketClientCompressionHandler.INSTANCE);
                    p.addLast("webSocketClientHandler", new WebSocketClientHandler());
                }
            });
            // tcp 建立连接
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 60000; j++) {
                    b.connect(host, port).sync().get();
                }
                port++;
            }
            System.in.read();
        } finally {
            group.shutdownGracefully();
        }
    }
}
