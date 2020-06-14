package com.skjanyou.server.api.inter;

import com.skjanyou.server.api.bean.ServerConfig;


public interface Server {
	public Server addFilter( Filter filter );
	public Server handler( ServerHandler handler );
	/**
	 * 设置配置文件
	 * @param config
	 * @return
	 */
	public Server setConfig(ServerConfig config);
	
	public Server init();
	/**
	 * 启动服务
	 * @return
	 */
	public Server startup();
	/**
	 * 服务是否存活
	 * @return
	 */
	public boolean isAlive();
	/**
	 * 关闭服务
	 * @return 
	 */
	public Server shutdown();
}
