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
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 打开输入流注解
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface LoadInputStream {}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2018年5月19日
	 * 作用 : 打开输出流注解
	 */	
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented	
	public @interface LoadOutputStream {}	
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年1月27日
	 * 作用 : 别名注解
	 */	
	@Target( {ElementType.TYPE,ElementType.FIELD} )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Alias {
		String value();
	}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年1月27日
	 * 作用 : Property注解,填充Property中的数据
	 */		
	@Target( { ElementType.TYPE,ElementType.FIELD } )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Property {
		String value() ;
	}	
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年6月7日
	 * 作用 : PropertyBean注解,从Property中获取类,然后实例化成对象
	 */		
	@Target( { ElementType.TYPE,ElementType.FIELD } )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface PropertyBean {
		String value() ;
	}		
}
