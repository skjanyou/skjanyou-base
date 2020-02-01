package com.skjanyou.mvc.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.mvc.handler.CharacterEncodingFilter;
import com.skjanyou.mvc.handler.MvcHandler;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpServer;
import com.skjanyou.util.StringUtil;

public class MvcHttpPlugin implements PluginSupport{
	@Property("mvc.port")
	private String port;
	@Property("mvc.ip")
	private String ip;
	@Property("mvc.timeout")
	private String timeout;
	@Property("mvc.scanPath")
	private String scanPath;
	
	private ServerConfig config;
	private Server server;
	private Logger logger = LogUtil.getLogger(MvcHttpPlugin.class);
	
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		config = new ServerConfig();
		config.setIp(ip);
		config.setPort(Integer.parseInt(port));
		if( !StringUtil.isBlank(timeout) ){
			config.setTimeout(Long.parseLong(timeout));
		}
		ApplicateContext.setServerHandler(new MvcHandler(scanPath));
    	ApplicateContext.registFilter(new CharacterEncodingFilter());	
	}

	@Override
	public void startup() {
    	server = new HttpServer(config);
		server.startup();
		logger.info("MVC服务器启动成功,信息:{ IP:" + config.getIp() + ",端口:" + config.getPort() + "}");
	}

	@Override
	public void shutdown() {
		server.shutdown();
	}

}
