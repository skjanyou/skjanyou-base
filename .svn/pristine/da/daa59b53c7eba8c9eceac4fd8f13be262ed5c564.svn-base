package com.skjanyou.annotation.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface Util {
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 :	 可以从配置文件中取得变量
	 */
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface Value {}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日 </br>
	 * 作用 : 快捷获取文件对象的注解 </br>
	 * 放在成员变量上,加载文件到这个变量 </br>
 	 */
	@Target({ ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface file {}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 创建一个新实例
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface newInstance {}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 非空注解
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface notNull {}
	
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface LoadInputStream {}
	
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface LoadOutputStream {}	
	
}
