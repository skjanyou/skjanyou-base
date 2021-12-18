package com.skjanyou.server.nettyhttpserver.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Application.Plugin;
import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.annotation.api.Util.PropertyBean;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.server.api.bean.FilterAdapter;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.nettyhttpserver.http.NettyHttpServer;
import com.skjanyou.util.StringUtil;

@Plugin
public class NettyHttpServerPlugin implements PluginSupport{
	private Logger logger = LogUtil.getLogger(NettyHttpServerPlugin.class);
	@Property("nettyhttpserver.ip")
	private String ip;
	@Property("nettyhttpserver.port")
	private String port;
	@Property("nettyhttpserver.timeout")
	private String timeout;
	@Property("nettyhttpserver.use")
	private String use;
	
	private ServerConfig config;
	@PropertyBean("nettyhttpserver")
	private Server server;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties)  throws Exception {
		if( !Boolean.valueOf(use) ){
			return ;
		}
		logger.info("因为系统配置[nettyhttpserver.use=" + use + "],所以nettyhttpserver即将启动,若需要关闭需要将该项设置为false。");
		
		config = new ServerConfig();
		config.setIp(ip);
		config.setPort(Integer.parseInt(port));
		if( !StringUtil.isBlank(timeout) ){
			config.setTimeout(Long.parseLong(timeout));
		}
	}

	@Override
	public void startup()  throws Exception {
		if( !Boolean.valueOf(use) ){
			logger.error("因为系统配置[nettyhttpserver.use=" + use + "],所以nettyhttpserver未启动,若需要启动需要移除该项配置或者将设置为true。");
			return ;
		}
		server = new NettyHttpServer();
		server.setConfig(config);
		server.addFilter(new FilterAdapter());
		server.handler(new ServerHandler() {
			
			@Override
			public ServerHandler init() throws ServerException {
				return this;
			}
			
			@Override
			public void handler(Request request, Response response)
					throws ServerException {
				
			}
		});
		server.init();
		server.startup();
		logger.info("nettyhttpserver服务器启动成功,信息:{ IP:" + config.getIp() + ",端口:" + config.getPort() + "}");
	}

	@Override
	public void shutdown()  throws Exception {
		if( server != null ){
			server.shutdown();
		}
	}

}
