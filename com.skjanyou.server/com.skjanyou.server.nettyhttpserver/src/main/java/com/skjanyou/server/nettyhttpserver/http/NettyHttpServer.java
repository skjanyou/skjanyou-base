package com.skjanyou.server.nettyhttpserver.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.AbstractServer;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.api.inter.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class NettyHttpServer extends AbstractServer {
	private ServerBootstrap serverBootstrap;
	/** 接收线程个数, 默认cpu个数 */
	private int acceptThreadSize;
	/** 业务处理线程个数，默认接收线程的2倍 */
	private int workerThreadSize;
	private int readTimeout = 1;
	private int writeTimeout = 1;
	private String svrIp = "127.0.0.1";
	private int srvPort;
	private URL url = null;
	
	private EventLoopGroup bossGroup; // 接收线程

	private EventLoopGroup workerGroup; // 工作线程
	
	private ExecutorService bizThreadPool;
	
	public NettyHttpServer() {}

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
	public Server startup() {
		ChannelFuture chanelFuture = ((svrIp != null && svrIp.length() > 0) ? serverBootstrap.bind(svrIp, this.srvPort)
				: serverBootstrap.bind(svrIp,getConfig().getPort()));

		try {
			chanelFuture.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		return this;
	}

	@Override
	public Server init() {
		this.acceptThreadSize = Runtime.getRuntime().availableProcessors();
		this.workerThreadSize = acceptThreadSize * 2;		
		this.srvPort = getConfig().getPort();
		try {
			this.url = new URL(String.format("http://%s:%d", svrIp,this.srvPort));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		this.serverBootstrap = new ServerBootstrap();
		this.bossGroup = new NioEventLoopGroup(acceptThreadSize);
		this.workerGroup = new NioEventLoopGroup(workerThreadSize);
		this.serverBootstrap.group(bossGroup, workerGroup);
		this.serverBootstrap.channel(NioServerSocketChannel.class);
		this.serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		this.serverBootstrap.option(ChannelOption.ALLOW_HALF_CLOSURE, true)
		.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024*1024));
		this.serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast("readTimeOut", new ReadTimeoutHandler(readTimeout)); // 读超时。
				pipeline.addLast("writeTimeOut", new WriteTimeoutHandler(writeTimeout)); //// 写超时。
                // 请求解码器
				pipeline.addLast("http-decoder", new HttpRequestDecoder());
                // 将HTTP消息的多个部分合成一条完整的HTTP消息
                pipeline.addLast("http-aggregator", new HttpObjectAggregator(1024 * 128));
                // 响应转码器
                pipeline.addLast("http-encoder", new HttpResponseEncoder());
                // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
				pipeline.addLast(new NioShortServerHandler(url, bizThreadPool,NettyHttpServer.this));
				
			}
		});		
		this.bizThreadPool = Executors.newFixedThreadPool(20);
		for( Filter filter : this.filters){
			filter.init();
		};		
		this.handler.init();
		return this;
	}
	
	/**
	 * 方便Dispatch获取信息
	 * @return
	 */
	ServerConfig getConfig(){
		return this.config;
	}
	
	List<Filter> getFilters(){
		return this.filters;
	}
	
	ServerHandler getHandler(){
		return this.handler;
	}	
	

}
