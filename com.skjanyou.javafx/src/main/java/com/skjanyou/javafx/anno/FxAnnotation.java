package com.skjanyou.javafx.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.javafx.constant.ControllerExitType;
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
		/** 退出方式 **/
		ControllerExitType exitType() default ControllerExitType.HIDE ;
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
	 * 	时间 : 2021-9-23
	 * 	作用 : 该注解用于给Controller中的公共组件赋值,例如当前页面的Stage、Scene
	 */
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxContext {
		
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
	public static @interface FxBean{
		public static enum BindType {
			BYNAME,
			BYID,
			BYFORM
		}
		String[] value() default {};
	}
	
	/**
	 * 
	 * 	@author skjanyou
	 * 	时间 : 2021-11-11
	 * 	<pre>
	 * 	作用 : Fx生命周期,不想实现ControllerLifeCycle的话,可以通过该注解来实现生命周期的监听　</br>
	 *      
	 */	
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxLifeCycle{
		public static enum FxLifeCycleType {
			ONLOAD,
			ONINIT,
			ONSHOW,
			ONDESTROY
		}
		FxLifeCycleType[] value() default {};
	}
	
	/**
	 * 
	 * 	@author skjanyou
	 * 	时间 : 2020-10-7
	 * 	<pre>
	 * 	作用 : 元素的校验,支持自定义校验规则　</br>
	  *   只能使用在节点上面,必须和@FXML同用
	 *  </pre>
	 *      
	 */	
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxValidate{
		/** 表单 **/
		String form() default "";
		/** 校验规则 **/
		FxValidateRule[] value();
	}
	
	/**
	 * 这里做一个约定,后置规则的调用频率一定要小于等于前置规则</br>
	 * 例如：前置规则为required,触发方式为blur；后置规则为card,那么触发方式就不能选择change,只能选择blur
	 * @author skjanyou
	 * 时间 : 2021-11-12
	 * 作用 :
	 */
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface FxValidateRule{
		/** 规则名字 **/
		String rule();
		/** 触发方式 **/
		RuleValidTrigger trigger() default RuleValidTrigger.CHANGE;
		/** 参数 **/
		String[] param() default {};
		/** 前置规则名字,只有前置规则满足后,才会走这个规则 **/
		String preRule() default "";
		
		public static enum RuleValidTrigger {
			BLUR,
			CHANGE,
		}
	}
}
