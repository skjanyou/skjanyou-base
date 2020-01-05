package com.skjanyou.server.simplehttpserver;

import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpServer;
import com.skjanyou.server.simplehttpserver.http.HttpServerHandler;

public class AppTest {
	public static void main(String[] args) {
		ApplicateContext.setServerHandler(new HttpServerHandler());
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
				return null;
			}
		});
		ServerConfig config = new ServerConfig();
		config.setIp("127.0.0.1");config.setPort(2333);config.setTimeout(5000000);
		Server server = new HttpServer(config);
		server.startup();
	}
}
