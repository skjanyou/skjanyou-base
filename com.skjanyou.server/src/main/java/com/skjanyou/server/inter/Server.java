package com.skjanyou.server.inter;


public interface Server {
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
