package com.skjanyou.mvc.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Mvc {
	@Documented
	@Inherited
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Controller {
		public String value() default "";
	}	
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Mapping {
		public String value() default "";
	}		
	
	@Documented
	@Inherited
	@Target({ElementType.TYPE,ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Service {
		public String value() default "";
	}		
	
	@Documented
	@Inherited
	@Target({ElementType.TYPE,ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Dao {
		public String value() default "";
	}		
	
	@Documented
	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)	
	public @interface Autowired {
		public String value() default "";
	}
	
	@Documented
	@Inherited
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)	
	public @interface HttpParameter {
		public String value();
	}	
}
