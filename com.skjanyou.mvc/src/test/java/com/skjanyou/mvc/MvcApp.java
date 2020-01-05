package com.skjanyou.mvc;

import com.skjanyou.mvc.handler.MvcHandler;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpServer;

public class MvcApp {

	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setIp("");config.setPort(2333);config.setTimeout(5000000000L);
		ApplicateContext.setServerHandler(new MvcHandler("com.skjanyou.mvc"));
		Server server = new HttpServer(config);
		server.startup();
	}

}
