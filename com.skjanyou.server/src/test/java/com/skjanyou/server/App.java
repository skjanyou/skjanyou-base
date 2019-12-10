package com.skjanyou.server;

import com.skjanyou.server.bean.ServerConfig;
import com.skjanyou.server.impl.http.HttpServer;
import com.skjanyou.server.inter.Server;

public class App {
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setIp("127.0.0.1");config.setPort(2333);config.setTimeout(5000000);
		Server server = new HttpServer(config);
		server.startup();
	}
}
