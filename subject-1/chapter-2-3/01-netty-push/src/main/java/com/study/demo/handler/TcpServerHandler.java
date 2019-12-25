package com.study.demo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.LongAdder;

public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {

    public static final LongAdder counter = new LongAdder();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        counter.add(1);
        System.out.println("Current conn size: " + counter.longValue());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        counter.decrement();
        System.out.println("Current conn size: " + counter.longValue());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
