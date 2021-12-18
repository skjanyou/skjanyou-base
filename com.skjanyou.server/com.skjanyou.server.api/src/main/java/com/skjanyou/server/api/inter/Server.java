package com.skjanyou.server.api.inter;

import com.skjanyou.server.api.bean.ServerConfig;


public interface Server {
	/**
	 * 获取过滤器处理器
	 * @return
	 */
	public FilterHandler getFilterHandler();
	/**
	 * 添加过滤器
	 * @param filter
	 * @return
	 */
	public Server addFilter( Filter filter );
	/**
	 * 添加处理器
	 * @param handler
	 * @return
	 */
	public Server handler( ServerHandler handler );
	/**
	 * 设置配置文件
	 * @param config
	 * @return
	 */
	public Server setConfig(ServerConfig config);
	/**
	 * 初始化
	 * @return
	 */
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
