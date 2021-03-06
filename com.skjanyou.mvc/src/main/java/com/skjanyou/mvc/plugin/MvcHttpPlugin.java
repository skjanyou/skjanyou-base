package com.skjanyou.mvc.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.annotation.api.Util.PropertyBean;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.mvc.filter.CharacterEncodingFilter;
import com.skjanyou.mvc.handler.AutowireMvcHandler;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Server;

public class MvcHttpPlugin implements PluginSupport{
	@Property("mvc.port")
	private int port;
	@Property("mvc.ip")
	private String ip;
	@Property("mvc.timeout")
	private Long timeout;
	@Property("mvc.scanPath")
	private String scanPath;
	@PropertyBean("mvc.httpserverclass")
	private Server server;
	@PropertyBean("mvc.filters")
	private List<Filter> filters;
	private ServerConfig config;
	private Logger logger = LogUtil.getLogger(MvcHttpPlugin.class);
	
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		config = new ServerConfig();
		config.setIp(ip);
		config.setPort(port);
		if( timeout != null ){
			config.setTimeout(timeout);
		}
		server.handler(new AutowireMvcHandler(scanPath));
		server.addFilter(new CharacterEncodingFilter());
		this.filters.forEach( filter -> {
			server.addFilter(filter);
		});
    	server.setConfig(config);
	}

	@Override
	public void startup() {
		server.init();
		server.startup();
		logger.info("MVC服务器启动成功,信息:{ IP:" + config.getIp() + ",端口:" + config.getPort() + "}");
	}

	@Override
	public void shutdown() {
		server.shutdown();
	}

}
