package com.skjanyou.server.simplehttpserver.http;

import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.AbstractServer;

public class HttpServer extends AbstractServer {
	private DispatchThread dispatchThread;
	public HttpServer(){}
	public HttpServer(ServerConfig config) {
		super(config);
	}
	
	
	@Override
	public Server init(){
		for( Filter filter : ApplicateContext.getRegistedFilter()){
			filter.init();
		};
		try {
			ApplicateContext.getServerHandler().init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dispatchThread = new DispatchThread(this.config);
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
		for( Filter filter : ApplicateContext.getRegistedFilter()){
			filter.destroy();
		};		
		return this;
	}

	@Override
	public Server setConfig(ServerConfig config) {
		this.config = config;
		return this;
	}

}
