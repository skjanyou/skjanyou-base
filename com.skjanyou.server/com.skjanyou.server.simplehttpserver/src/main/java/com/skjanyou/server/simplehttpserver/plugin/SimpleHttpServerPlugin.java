package com.skjanyou.server.simplehttpserver.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Application.Plugin;
import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpServer;
import com.skjanyou.util.StringUtil;

@Plugin
public class SimpleHttpServerPlugin implements PluginSupport{
	private Logger logger = LogUtil.getLogger(SimpleHttpServerPlugin.class);
	@Property("simplehttpserver.ip")
	private String ip;
	@Property("simplehttpserver.port")
	private String port;
	@Property("simplehttpserver.timeout")
	private String timeout;
	@Property("simplehttpserver.use")
	private String use;
	
	private ServerConfig config;
	private Server server;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		if( !Boolean.valueOf(use) ){
			return ;
		}
		logger.info("因为系统配置[simplehttpserver.use=" + use + "],所以simplehttpserver即将启动,若需要关闭需要将该项设置为false。");
		ApplicateContext.setServerHandler(new ServerHandler() {
			
			@Override
			public ServerHandler init() throws ServerException {
				return this;
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
		config = new ServerConfig();
		config.setIp(ip);
		config.setPort(Integer.parseInt(port));
		if( !StringUtil.isBlank(timeout) ){
			config.setTimeout(Long.parseLong(timeout));
		}
	}

	@Override
	public void startup() {
		if( !Boolean.valueOf(use) ){
			logger.error("因为系统配置[simplehttpserver.use=" + use + "],所以simplehttpserver未启动,若需要启动需要移除该项配置或者将设置为true。");
			return ;
		}
		server = new HttpServer(config);
		server.startup();
		logger.info("simplehttpserver服务器启动成功,信息:{ IP:" + config.getIp() + ",端口:" + config.getPort() + "}");
	}

	@Override
	public void shutdown() {
		if( server != null ){
			server.shutdown();
		}
	}

}
