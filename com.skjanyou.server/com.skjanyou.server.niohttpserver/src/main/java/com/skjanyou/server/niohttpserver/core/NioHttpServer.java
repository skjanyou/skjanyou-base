package com.skjanyou.server.niohttpserver.core;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.AbstractServer;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;

public class NioHttpServer extends AbstractServer {
	private Selector selector;

	public NioHttpServer() throws Exception {
		selector = Selector.open();

		// 打开服务端socket通道
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 设置非阻塞
		ssc.configureBlocking(false);
		// 绑定本地端口
		ssc.bind(new InetSocketAddress(config.getPort()));
		// 把通道注册到Selector
		ssc.register(selector, SelectionKey.OP_ACCEPT);


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
			while (true) {
				
				if (selector.select() > 0) {
					
					// 获取已经就绪的通道的SelectionKey的集合
					Iterator<SelectionKey> i = selector.selectedKeys().iterator();
					
					while (i.hasNext()) {
						
						// 获取当前遍历到的SelectionKey
						SelectionKey sk = i.next();
						// 移除key
						i.remove();
						
						try {
						} catch (Exception e) {
							sk.cancel();
							sk = null;
						}
					}
				}
			}
		} catch(Exception e) {
			
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
