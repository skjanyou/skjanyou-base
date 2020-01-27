package com.skjanyou.server.simplehttpserver.plugin;

import java.util.List;

import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.AppTest.MyServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpServer;


public class SimpleHttpServerPlugin implements PluginSupport{
	private Server server;
	private ServerConfig config;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass) {
		
	}

	@Override
	public void startup() {
		ApplicateContext.setServerHandler(new ServerHandler() {
			
			@Override
			public ServerHandler init() throws ServerException {
				return null;
			}
			
			@Override
			public void handler(Request request, Response response)
					throws ServerException {
				
			}
		});
		ApplicateContext.registFilter(new Filter() {
			
			@Override
			public int priority() {
				return 0;
			}
			
			@Override
			public Filter init() {
				return this;
			}
			
			@Override
			public boolean doFilter(Request request, Response response)
					throws Exception {
				return true;
			}
			
			@Override
			public Filter destroy() {
				return this;
			}
		});
		this.config = new ServerConfig();
		this.config.setIp("127.0.0.1");
		this.config.setPort(2333);
		this.server = new HttpServer(config);
		this.server.startup();		
	}

	@Override
	public void shutdown() {
		this.server.shutdown();
	}

}
