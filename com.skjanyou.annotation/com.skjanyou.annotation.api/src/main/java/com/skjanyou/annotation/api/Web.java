package com.skjanyou.annotation.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.annotation.api.enumclass.RequestType;
import com.skjanyou.annotation.api.enumclass.ResponseType;


/**
 * 
 * @author skjanyou
 * 时间 : 2018年5月19日
 * 作用 : Web类应用的注解
 * 
 */
public final class Web {
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : Http请求
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Request {
		String mapping() default "/";
		RequestType type() default RequestType.ALL;
	}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : Http响应
	 */
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Response {
		ResponseType type() default ResponseType.HTML;
	}	
}
