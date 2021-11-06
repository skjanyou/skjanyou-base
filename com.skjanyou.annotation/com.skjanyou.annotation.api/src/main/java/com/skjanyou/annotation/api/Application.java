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
	 * 作用 : 自动从容器中取出bean来填充的注解,需要使用容器才有效,并且需要配合Component注解才有效
	 */
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented		
	public @interface Autowired {
		String value() default "";
	}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年1月27日
	 * 作用 : 标记bean的注解,需要使用容器才有效,并且需要配合Component注解才有效,仅能添加到方法上面,提供动态创建Bean的功能,创建后的bean会放入到容器中
	 */	
	@Target( { ElementType.TYPE,ElementType.FIELD } )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Bean {
		String value() default "";
	}
	
	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年1月27日
	 * 作用 : 标记组件的注解,需要使用容器才有效,标记Component的类,会处理内部成员变量的注解
	 */		
	@Target( { ElementType.TYPE } )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Component {
		
	}

	/**
	 * 
	 * @author skjanyou
	 * 时间 : 2020年1月29日
	 * 作用 : 标记插件的注解,标记注解的类,会可以使用使用@Property注解对成员变量进行赋值
	 */		
	@Target( { ElementType.TYPE } )
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Plugin {
		
	}
}
