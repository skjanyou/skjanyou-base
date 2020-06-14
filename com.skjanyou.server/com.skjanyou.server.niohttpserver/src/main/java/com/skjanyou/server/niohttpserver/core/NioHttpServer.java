package com.skjanyou.server.niohttpserver.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.AbstractServer;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;

public class NioHttpServer extends AbstractServer {
	private ServerBootstrap bootstrap;
	private EventLoopGroup boss;
	private EventLoopGroup work;
	private ChannelFuture future;
	

	public NioHttpServer() {
		bootstrap  = new ServerBootstrap();
		boss = new NioEventLoopGroup();
		work = new NioEventLoopGroup();
		
		
		bootstrap.group(boss,work)
		.handler(new LoggingHandler(LogLevel.DEBUG))
		.channel(NioServerSocketChannel.class)
		.childHandler(new NioHttpServerInitializer());

	}

	@Override
	public Server setConfig(ServerConfig config) {
		return this;
	}

	@Override
	public Server init() {
		for( Filter filter : this.filters){
			filter.init();
		};		
		this.handler.init();
		return this;
	}

	@Override
	public Server startup() {
		try {
			future = bootstrap.bind(new InetSocketAddress(this.config.getPort())).sync();
			System.out.println(" server start up on port : " + this.config.getPort());
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		return this;
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public Server shutdown() {
		for( Filter filter : this.filters){
			filter.destroy();
		};	
		return this;
	}

	@Override
	public Server addFilter(Filter filter) {
		this.filters.add(filter);
		return this;
	}

}
