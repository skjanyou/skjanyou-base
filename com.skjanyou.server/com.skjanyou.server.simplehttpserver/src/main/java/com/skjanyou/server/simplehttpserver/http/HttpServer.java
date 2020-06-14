package com.skjanyou.server.simplehttpserver.http;

import java.util.List;

import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.AbstractServer;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.api.inter.ServerHandler;

public class HttpServer extends AbstractServer {
	private DispatchThread dispatchThread;
	public HttpServer(){}
	
	
	@Override
	public Server init(){
		for( Filter filter : this.filters){
			filter.init();
		};		
		this.handler.init();		
		dispatchThread = new DispatchThread(this);
		return this;
	}
	

	@Override
	public Server startup() {
		if( dispatchThread == null ){
			throw new RuntimeException("服务启动失败:[线程创建失败]");
		}
		dispatchThread.start();
		return this;
	}

	@Override
	public boolean isAlive() {
		return dispatchThread.isRunning();
	}

	@Override
	public Server shutdown() {
		if( dispatchThread != null ){
			dispatchThread.setRunning(false);
		}
		for( Filter filter : this.filters){
			filter.destroy();
		};			
		return this;
	}

	@Override
	public Server setConfig(ServerConfig config) {
		this.config = config;
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
