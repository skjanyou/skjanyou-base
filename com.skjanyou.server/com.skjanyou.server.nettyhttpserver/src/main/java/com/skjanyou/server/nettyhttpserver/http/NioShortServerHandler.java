package com.skjanyou.server.nettyhttpserver.http;

import java.net.URL;
import java.util.concurrent.ExecutorService;

import com.skjanyou.server.nettyhttpserver.util.NettyUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class NioShortServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>  {
	private NettyHttpServer nettyHttpServer;


	boolean hasReceived = false;

	private ExecutorService bizThreadPool;

	public NioShortServerHandler(URL protocolUrl, ExecutorService bizThreadPool, NettyHttpServer nettyHttpServer) {
		this.nettyHttpServer = nettyHttpServer;
		this.bizThreadPool = bizThreadPool;
	}

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		byte[] msgBytes = NettyUtil.getRequestBytes(msg);
		bizThreadPool.execute(new NettyRequestProcessTask(this.nettyHttpServer,ctx, msgBytes));
	}
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		ctx.close();
	}

}
