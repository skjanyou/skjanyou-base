package com.skjanyou.mvc;

import com.skjanyou.mvc.filter.CharacterEncodingFilter;
import com.skjanyou.mvc.handler.MvcHandler;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpServer;

public class MvcApp {

	public static void main(String[] args) {
		Server server = new HttpServer();
		ServerConfig config = new ServerConfig();
		config.setIp("");config.setPort(2333);config.setTimeout(5000000000L);
		server.handler(new MvcHandler("com.skjanyou.mvc"));
		server.addFilter(new CharacterEncodingFilter());	
    	server.setConfig(config);
		server.startup();
	}

}
