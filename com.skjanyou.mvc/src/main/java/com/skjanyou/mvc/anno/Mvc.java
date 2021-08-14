package com.skjanyou.mvc.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import com.skjanyou.server.api.exception.ServerException;

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
		public boolean required() default true;
	}	
	
	@Documented
	@Inherited
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)	
	public @interface HttpPostReuqestBody {
		public String value() default "";
		public boolean required() default true;
	}
	
	@Documented
	@Inherited
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)	
	public @interface Transactional {
		public Level level();
		public static enum Level {
			NONE,ALL
		}
	}
	
	@Documented
	@Inherited
	@Target({ElementType.TYPE,ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface HandlerException {
		public Class<? extends Exception>[] exception() default { Exception.class };
		public Class<? extends ExceptionHandler<? extends Exception>> handler() default DefaultExceptionHandler.class;
		
		public static interface ExceptionHandler<T extends Throwable> {
			public Object handler( T exception, Object target, Method method, Object input );
		}
		
		class DefaultExceptionHandler implements ExceptionHandler<Exception>{
			@Override
			public Object handler(Exception exception, Object target, Method method, Object input) {
				exception.printStackTrace();
				throw new ServerException("方法调用失败:" + method,exception);
			}
		}
	}	
}
