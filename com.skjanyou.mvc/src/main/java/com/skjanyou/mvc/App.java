package com.skjanyou.mvc;

import com.skjanyou.mvc.handler.MvcHandler;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String scanPath = "com.skjanyou";
    	ApplicateContext.setServerHandler(new MvcHandler(scanPath));
    	ServerConfig config = new ServerConfig();
    	config.setIp("127.0.0.1");config.setPort(4455);config.setTimeout(5000000);
        Server server = new HttpServer(config);
        server.startup();
    }
}
