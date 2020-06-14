package com.skjanyou.mvc;

import com.skjanyou.mvc.filter.CharacterEncodingFilter;
import com.skjanyou.mvc.filter.StaticFileFilter;
import com.skjanyou.mvc.handler.MvcHandler;
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
    	Server server = new HttpServer();
    	String scanPath = "com.skjanyou";
    	String staticFilePath = "D:\\";
    	server.handler(new MvcHandler(scanPath));
    	server.addFilter(new CharacterEncodingFilter());
    	server.addFilter(new StaticFileFilter(staticFilePath));
    	ServerConfig config = new ServerConfig();
    	config.setIp("127.0.0.1");config.setPort(4455);config.setTimeout(5000000);
        server.setConfig(config);
        server.startup();
    }
}
