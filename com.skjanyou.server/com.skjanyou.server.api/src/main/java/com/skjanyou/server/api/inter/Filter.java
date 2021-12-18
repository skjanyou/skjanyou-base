package com.skjanyou.server.api.inter;

import com.skjanyou.server.api.bean.HttpEntity;
import com.skjanyou.util.plus.chain.Chain;

public interface Filter extends Chain<HttpEntity> {
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
