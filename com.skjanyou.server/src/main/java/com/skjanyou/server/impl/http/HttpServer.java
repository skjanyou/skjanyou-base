package com.skjanyou.server.impl.http;

import com.skjanyou.server.bean.ServerConfig;
import com.skjanyou.server.impl.AbstractServer;
import com.skjanyou.server.inter.Server;

public class HttpServer extends AbstractServer {
	private DispatchThread dispatchThread;
	public HttpServer(ServerConfig config) {
		super(config);
	}
	
	@Override
	public Server init(){
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
		return false;
	}

	@Override
	public Server shutdown() {
		return null;
	}

}
