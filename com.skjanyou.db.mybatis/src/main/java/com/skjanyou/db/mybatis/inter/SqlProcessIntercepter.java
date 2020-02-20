package com.skjanyou.db.mybatis.inter;

import com.skjanyou.db.mybatis.bean.IntercepterSqlInfo;

/**
 * Sql处理拦截器
 * @author skjanyou
 * 时间 : 2020年2月20日
 * 作用 :
 */
public interface SqlProcessIntercepter<T> {
	/**
	 * 前置拦截,在执行sql语句之前执行
	 */
	public void beforeIntercepter( IntercepterSqlInfo intercepterSqlInfo );
	/**
	 * 后置拦截,已经执行完sql语句
	 */
	public void afterIntercepter( Object result );
}
