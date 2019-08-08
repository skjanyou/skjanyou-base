package com.skjanyou.annotation.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author skjanyou
 * 时间 : 2018年5月19日
 * 作用 :	 应用级别的注解
 */
public final class Application {
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 用于应用启动类,表示这个类为应用程序的入口,可以给程序配置参数格式为 "key:value"
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface ApplicationClass{
		String[] args() default "";
	}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 自动从容器中取出bean来填充的注解,需要使用容器才有效
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented		
	public @interface Autowired {

	}
}
