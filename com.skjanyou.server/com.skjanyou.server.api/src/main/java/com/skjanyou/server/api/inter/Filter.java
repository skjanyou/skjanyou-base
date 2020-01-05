package com.skjanyou.server.api.inter;

public interface Filter {
	/**
	 * 过滤器优先级,优先级越大,越先执行
	 * @return
	 */
	public int priority();
	/**
	 * 初始化过滤器
	 * @return
	 */
	public Filter init();
	/**
	 * 过滤
	 * @param request   请求
	 * @param response  响应
	 * @return  boolean  是否能够继续执行下一个过滤器
	 */
	public boolean doFilter( Request request,Response response ) throws Exception;
	/**
	 * 过滤器销毁
	 * @return
	 */
	public Filter destroy();
}
