package com.skjanyou.mvc;

import com.skjanyou.mvc.handler.MvcHandler;
import com.skjanyou.server.bean.ApplicateContext;
import com.skjanyou.server.bean.ServerConfig;
import com.skjanyou.server.impl.http.HttpServer;
import com.skjanyou.server.inter.Server;

public class MvcApp {

	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setIp("");config.setPort(2333);config.setTimeout(5000000000L);
		ApplicateContext.setServerHandler(new MvcHandler("com.skjanyou.mvc"));
		Server server = new HttpServer(config);
		server.startup();
	}

}
