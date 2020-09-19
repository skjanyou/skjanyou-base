package com.skjanyou.db.mybatis.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.skjanyou.db.mybatis.bean.Invocation;
import com.skjanyou.db.mybatis.exception.DaoException;
import com.skjanyou.db.mybatis.inter.AnnotationHandler;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;
import com.skjanyou.db.mybatis.util.BeanUtil;
import com.skjanyou.db.mybatis.util.SqlUtil;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;

public final class BeanAnnotation {
	
	public static class SelectOneAnnotationHandler implements AnnotationHandler<SelectOne>{
		@Override
		public Object handler(Invocation<SelectOne> item) {
			Object result = null;
			SelectOne selectOne = item.getAnno();
			Class<? extends SqlProcess<SelectOne>> sqlProcessClass = selectOne.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = selectOne.exception();
			
			SqlProcess<SelectOne> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}
	
	public static class SelectFirstAnnotationHandler implements AnnotationHandler<SelectFirst>{
		@Override
		public Object handler(Invocation<SelectFirst> item) {
			Object result = null;
			SelectFirst selectFirst = item.getAnno();
			Class<? extends SqlProcess<SelectFirst>> sqlProcessClass = selectFirst.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = selectFirst.exception();
			
			SqlProcess<SelectFirst> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}	
	
	public static class InsertAnnotationHandler implements AnnotationHandler<Insert>{
		@Override
		public Object handler(Invocation<Insert> item) {
			Object result = null;
			Insert insert = item.getAnno();
			Class<? extends SqlProcess<Insert>> sqlProcessClass = insert.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = insert.exception();
			
			SqlProcess<Insert> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}	
	
	public static class QueryAnnotationHandler implements AnnotationHandler<Query>{
		@Override
		public Object handler(Invocation<Query> item) {
			Object result = null;
			Query query = item.getAnno();
			Class<? extends SqlProcess<Query>> sqlProcessClass = query.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = query.exception();
			
			SqlProcess<Query> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}		
	
	public static class QueryPageAnnotationHandler implements AnnotationHandler<QueryPage>{
		@Override
		public Object handler(Invocation<QueryPage> item) {
			Object result = null;
			QueryPage queryPage = item.getAnno();
			Class<? extends SqlProcess<QueryPage>> sqlProcessClass = queryPage.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = queryPage.exception();
			
			SqlProcess<QueryPage> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}	
	
	public static class UpdateAnnotationHandler implements AnnotationHandler<Update>{
		@Override
		public Object handler(Invocation<Update> item) {
			Object result = null;
			Update update = item.getAnno();
			Class<? extends SqlProcess<Update>> sqlProcessClass = update.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = update.exception();
			
			SqlProcess<Update> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
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
			private Logger logger = LogUtil.getLogger(SelectOneProcess.class);
			@Override
			public void handlerException(Throwable throwable) {
				logger.error(throwable);
			}
			@Override
			public Object process(Invocation<SelectOne> pi) {
				// 获取Mapper类的泛型
				Class<?> mapperClass = pi.getMapperClass();
				ParameterizedType pt = (ParameterizedType )mapperClass.getGenericInterfaces()[0];
				Type trueType = pt.getActualTypeArguments()[0];
				Class<?> typeClass = (Class<?>)trueType;
				// 参数检查
				if( pi.getArgs().length != 1 || pi.getArgs()[0].getClass() != typeClass ){
					throw new DaoException("查询参数数量必须为1,并且类型为"+typeClass.getName());
				}
				Object bean = pi.getArgs()[0];
				String sql = SqlUtil.generateSelectSQL(typeClass);
				List<?> list = SqlSession.executeSelectListSql(sql, bean, typeClass,pi.getDataSourceManager());
				if( list.size() > 1 ){
					throw new DaoException("查询的到的数据量为" + list.size() + "大于0");
				}else if( list.size() == 0 ){
					return null;
				}
				return list.get(0);
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
				throw new RuntimeException(throwable);
			}
			@Override
			public Object process(Invocation<Query> pi) {
				// 获取Mapper类的泛型
				Class<?> mapperClass = pi.getMapperClass();
				ParameterizedType pt = (ParameterizedType )mapperClass.getGenericInterfaces()[0];
				Type trueType = pt.getActualTypeArguments()[0];
				Class<?> typeClass = (Class<?>)trueType;
				// 参数检查
				if( pi.getArgs().length != 0 || pi.getArgs()[0].getClass() != typeClass ){
					throw new DaoException("查询参数数量必须为0");
				}
				Object bean = new Object();
				String sql = SqlUtil.generateSelectSQL(typeClass);
				List<?> list = SqlSession.executeSelectListSql(sql, bean, typeClass,pi.getDataSourceManager());
				return list;	
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
