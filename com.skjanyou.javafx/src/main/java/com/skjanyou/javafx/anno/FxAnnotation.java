package com.skjanyou.javafx.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.javafx.constant.ControllerType;
import com.skjanyou.javafx.constant.DecoratorType;
import com.skjanyou.javafx.inter.JavaFxDecorator;
import com.skjanyou.javafx.inter.impl.JavaFxBlackDecorator;

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
		boolean bubble() default true;
		/**
		 *	选择器 
		 */
		String[] selectors();
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
		String icon() default "classpath:icon/liulian.png";
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
		/** 控制器类型 **/
		ControllerType type() default ControllerType.WINDOW;
	}
	
	/**
	 * 用来标记是否进行展示的主controller,使用这个注解,必须配合使用@FxController注解
	 * @author skjanyou
	 * 时间 : 2021-8-21
	 * 作用 :
	 */
	@Documented
	@Inherited
	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface MainFxController{
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
	public static @interface FxDecorator {
		/** fxml文件 **/
		String fxml() default "jfx/BlackDecorator.fxml";
		/** 配置类 **/
		Class<? extends JavaFxDecorator> config() default JavaFxBlackDecorator.class;
		/** 装饰器类型 **/
		DecoratorType type() default DecoratorType.ALL;
		/** 是否可以调整大小 **/
		boolean resizeable() default true;
		/** 是否可以移动 **/
		boolean moveable() default true;
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
