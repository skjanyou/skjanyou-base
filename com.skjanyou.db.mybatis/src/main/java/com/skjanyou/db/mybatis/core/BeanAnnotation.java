package com.skjanyou.db.mybatis.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.db.mybatis.bean.Invocation;
import com.skjanyou.db.mybatis.inter.AnnotationHandler;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;

public final class BeanAnnotation {
	public static class BeanBaseHandler implements AnnotationHandler<Annotation>{
		@Override
		public Object handler(Invocation<Annotation> item) {
			return null;
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SelectOne {
		public String value() default "";
		public Class<? extends SqlProcess<SelectOne>> handler() default SelectOneProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default SelectOneProcess.class;
		
		class SelectOneProcess implements SqlProcess<SelectOne>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<SelectOne> pi) {
				return null;
			}
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SelectFirst {
		public String value() default "";
		public Class<? extends SqlProcess<SelectFirst>> handler() default SelectFirstProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default SelectFirstProcess.class;
		
		class SelectFirstProcess implements SqlProcess<SelectFirst>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<SelectFirst> pi) {
				return null;
			}
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Insert {
		public String value() default "";
		public Class<? extends SqlProcess<Insert>> handler() default InsertProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default InsertProcess.class;
		
		class InsertProcess implements SqlProcess<Insert>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<Insert> pi) {
				return null;
			}
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Query {
		public String value() default "";
		public Class<? extends SqlProcess<Query>> handler() default QueryProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default QueryProcess.class;
		
		class QueryProcess implements SqlProcess<Query>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<Query> pi) {
				return null;
			}
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface QueryPage {
		public String value() default "";
		public Class<? extends SqlProcess<QueryPage>> handler() default QueryPageProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default QueryPageProcess.class;
		
		class QueryPageProcess implements SqlProcess<QueryPage>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<QueryPage> pi) {
				return null;
			}
		}
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Update {
		public String value() default "";
		public Class<? extends SqlProcess<Update>> handler() default UpdateProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default UpdateProcess.class;
		
		class UpdateProcess implements SqlProcess<Update>,SqlExceptionProcess {
			@Override
			public void handlerException(Throwable throwable) {
				
			}
			@Override
			public Object process(Invocation<Update> pi) {
				return null;
			}
		}
	}	
}
