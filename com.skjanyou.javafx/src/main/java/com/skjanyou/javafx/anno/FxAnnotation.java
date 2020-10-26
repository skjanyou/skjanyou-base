package com.skjanyou.javafx.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class FxAnnotation {
	
	/**
	 * 	@author skjanyou
	 * 	时间 : 2020-10-7
	 * 	作用 : fx的事件监听注解,支持id、class、属性绑定
	 */
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxEventHandler {
		/**
		 * @return 是否可以冒泡
		 */
		boolean bubble() default false;
		/**
		 * 	通过Id选择器进行选择
		 */
		String id() default "";
		/**
		 * 	通过类选择器进行选择
		 */
		String clazz() default "";
		/**
		 * 	通过属性选择器进行选择
		 */
		String attr() default "";
		/**
		 *	混合型选择器 
		 */
		String[] complexSelectors() default {};
		/**
		 * 	监听的事件
		 */
		String[] eventType() ;
	}
	
	/**
	 * 
	 * 	@author skjanyou
	 * 	时间 : 2020-10-7
	 * 	作用 : 该注解用于标识一个Controller
	 */
	@Documented
	@Inherited
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxController{
		/** 窗口唯一ID **/
		String id();
		/** 窗口的图标 **/
		String icon() default "";
		/** 标题 **/
		String title() default "";
		/** fxml文件 **/
		String fxml() default "";
		/** 皮肤 **/
		String skin() default "";
		/** css样式表 **/
		String css() default "";
		/** 样式 **/
		String[] styles() default {};
	}
	
	/**
	 * 
	 * 	@author skjanyou
	 * 	时间 : 2020-10-13
	 * 	作用 : 该注解用于给窗体设置装饰
	 */
	@Documented
	@Inherited
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Decorator {
		
	}
	
	/**
	 * 
	 * 	@author skjanyou
	 * 	时间 : 2020-10-7
	 * 	<pre>
	 * 	作用 : 响应式bean,用于与页面进行双向绑定　</br>
	  *    不填充参数时,会按照{变量名$属性名}去搜索并绑定组件 </br>
	 *	填充参数时,会按照填充的参数进行绑定
	 *     不填充参数时等同填充参数时的下列形式
	 *     name = #student$name.text,
	 *     age= #student$age.text
	 *  </pre>
	 *      
	 */
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ResponsiveBean{
		public static enum BindType {
			BYNAME,
			BYID,
			BYFORM
		}
		String[] value() default {};
	}
	
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface LifeCycle{
		public static enum LifeCycleType {
			ONLOAD,
			ONINIT,
			ONSHOW,
			ONDESTROY
		}
		LifeCycleType[] value() default {};
	}
}
