package com.skjanyou.server;

import com.skjanyou.server.bean.ApplicateContext;
import com.skjanyou.server.bean.ServerConfig;
import com.skjanyou.server.impl.http.HttpServer;
import com.skjanyou.server.impl.http.HttpServerHandler;
import com.skjanyou.server.inter.Filter;
import com.skjanyou.server.inter.Request;
import com.skjanyou.server.inter.Response;
import com.skjanyou.server.inter.Server;

public class App {
	public static void main(String[] args) {
		ApplicateContext.setServerHandler(new HttpServerHandler());
		ApplicateContext.registFilter(new Filter() {
			
			@Override
			public int priority() {
				return 0;
			}
			
			@Override
			public Filter init() {
				return null;
			}
			
			@Override
			public boolean doFilter(Request request, Response response)
					throws Exception {
				return true;
			}
			
			@Override
			public Filter destroy() {
				return null;
			}
		});
		ServerConfig config = new ServerConfig();
		config.setIp("127.0.0.1");config.setPort(2333);config.setTimeout(5000000);
		Server server = new HttpServer(config);
		server.startup();
	}
}
