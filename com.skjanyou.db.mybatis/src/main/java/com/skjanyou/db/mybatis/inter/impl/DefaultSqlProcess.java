package com.skjanyou.db.mybatis.inter.impl;


import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.BatchInsert;
import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.SqlParameter;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.bean.IntercepterSqlInfo;
import com.skjanyou.db.mybatis.bean.Invocation;
import com.skjanyou.db.mybatis.core.MybatisManager;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.exception.DaoException;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;
import com.skjanyou.db.mybatis.inter.SqlProcessIntercepter;

public final class DefaultSqlProcess {
	public static class SelectSqlProcess implements SqlProcess<Select>,SqlExceptionProcess {
		@Override
		public Object process(Invocation<Select> item) {
			// 1.获取原始SQL
			String sql = item.getAnno().value();
			// 2.获取返回类型
			Class<?> resultClass = item.getMethod().getReturnType();
			if( List.class.isAssignableFrom(resultClass) ){
				Type genericType = item.getMethod().getGenericReturnType();
				if( genericType instanceof ParameterizedType ){
					ParameterizedType pType = (ParameterizedType) genericType;	
					Type[] actualTypes = pType.getActualTypeArguments();
					if( actualTypes.length == 1 && actualTypes[0] instanceof Class<?> ){
						resultClass = (Class<?>) actualTypes[0];
					}
				}
			}
			// 3.获取输入参数
			Object bean = null;
			Object[] objects = item.getArgs();
			if( objects == null ){
				// 没有输入参数时,默认入参为void
				bean = Void.TYPE;
			}else if( objects.length == 1){
				// 一个输入参数时,检查注解,若不存在注解,则该输入参数即为bean
				SqlParameter sqlParameter = item.getMethod().getParameters()[0].getAnnotation(SqlParameter.class);
				if( sqlParameter == null ){
					bean = objects[0];
				}else{
					Map<String,Object> beanMap = new HashMap<>();
					beanMap.put(sqlParameter.value(), objects[0]);
					bean = beanMap;
				}
			}else{
				Map<String,Object> beanMap = new HashMap<>();
				// 多个参数时,检查注解
				Parameter[] parameters = item.getMethod().getParameters();
				int length = parameters.length;
				Parameter parameter = null;
				SqlParameter sqlParameter = null;
				String sqlParmName = null;
				for( int i = 0;i < length;i++ ){
					parameter = parameters[i];
					sqlParameter = parameter.getAnnotation(SqlParameter.class);
					if( sqlParameter != null ){
						sqlParmName = sqlParameter.value();
						beanMap.put(sqlParmName, objects[i]);
					}
				}
				// 子类转父类
				bean = beanMap;
			}
			// 4.拦截处理
			IntercepterSqlInfo sqlInfo = new IntercepterSqlInfo(sql,bean);
			SqlProcessIntercepter<Select> selectIntercepter = MybatisManager.getSqlProcessIntercepter(Select.class);
			// 前置拦截
			if( selectIntercepter != null ){
				selectIntercepter.beforeIntercepter( sqlInfo );
			}
			// 5.返回结果
			List<?> resultList = SqlSession.executeSelectListSql(sqlInfo.getSql(), sqlInfo.getBean(), resultClass,item.getDataSourceManager());
			// 后置拦截
			if( selectIntercepter != null ){
				selectIntercepter.afterIntercepter( resultList );
			}
			
			
			// 5.1返回多条数据
			if( item.getMethod().getReturnType().isAssignableFrom(List.class) ){
				return resultList;
			}
			// 5.2返回单条数据
			if( resultList.size() == 0 ){
				return null;
			}else if( resultList.size() == 1 ){
				return resultList.get(0);
			}else{
				throw new DaoException("查询结果大于一条");
			}
		}

		@Override
		public void handlerException(Throwable throwable) {
			if( throwable instanceof DaoException){
				throw (DaoException)throwable;
			}
			throw new DaoException("执行查询SQL时出现异常",throwable);
		}
	}

	public static class InsertSqlProcess implements SqlProcess<Insert>,SqlExceptionProcess {
		@Override
		public Object process(Invocation<Insert> item) {
			// 1.获取原始SQL
			String sql = item.getAnno().value();
			// 2.获取返回类型
			Class<?> resultClass = item.getMethod().getReturnType();
			if( !Integer.class.isAssignableFrom(resultClass) && !int.class.isAssignableFrom(resultClass) ){
				throw new DaoException("插入SQL的返回类型必须为int类型");
			}
			// 3.获取输入参数
			Object bean = null;
			Object[] objects = item.getArgs();
			if( objects == null ){
				// 没有输入参数时,默认入参为void
				bean = Void.TYPE;
			}else if( objects.length == 1){
				// 一个输入参数时,检查注解,若不存在注解,则该输入参数即为bean
				SqlParameter sqlParameter = item.getMethod().getParameters()[0].getAnnotation(SqlParameter.class);
				if( sqlParameter == null ){
					bean = objects[0];
				}else{
					Map<String,Object> beanMap = new HashMap<>();
					beanMap.put(sqlParameter.value(), objects[0]);
					bean = beanMap;
				}
			}else{
				Map<String,Object> beanMap = new HashMap<>();
				// 多个参数时,检查注解
				Parameter[] parameters = item.getMethod().getParameters();
				int length = parameters.length;
				Parameter parameter = null;
				SqlParameter sqlParameter = null;
				String sqlParmName = null;
				for( int i = 0;i < length;i++ ){
					parameter = parameters[i];
					sqlParameter = parameter.getAnnotation(SqlParameter.class);
					if( sqlParameter != null ){
						sqlParmName = sqlParameter.value();
						beanMap.put(sqlParmName, objects[i]);
					}
				}
				// 子类转父类
				bean = beanMap;
			}
			// 4.拦截处理
			IntercepterSqlInfo sqlInfo = new IntercepterSqlInfo(sql,bean);
			SqlProcessIntercepter<Insert> selectIntercepter = MybatisManager.getSqlProcessIntercepter(Insert.class);
			// 前置拦截
			if( selectIntercepter != null ){
				selectIntercepter.beforeIntercepter( sqlInfo );
			}			
			// 5.返回结果
			Object result = SqlSession.executeInsertSql(sqlInfo.getSql(), sqlInfo.getBean(),item.getDataSourceManager());
			// 后置拦截
			if( selectIntercepter != null ){
				selectIntercepter.afterIntercepter( result );
			}
			
			return result;
		}

		@Override
		public void handlerException(Throwable throwable) {
			if( throwable instanceof DaoException){
				throw (DaoException)throwable;
			}
			throw new DaoException("执行插入SQL时出现异常",throwable);			
		}
	}
	
	public static class BatchInsertSqlProcess implements SqlProcess<BatchInsert>,SqlExceptionProcess {
		@Override
		public Object process(Invocation<BatchInsert> item) {
			// TODO 
			
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			if( throwable instanceof DaoException){
				throw (DaoException)throwable;
			}
			throw new DaoException("执行批量插入SQL时出现异常",throwable);			
		}
	}	
	
	public static class UpdateSqlProcess implements SqlProcess<Update>,SqlExceptionProcess {
		@Override
		public Object process(Invocation<Update> item) {
			// 1.获取原始SQL
			String sql = item.getAnno().value();
			// 2.获取返回类型
			Class<?> resultClass = item.getMethod().getReturnType();
			if( !Integer.class.isAssignableFrom(resultClass) && !int.class.isAssignableFrom(resultClass) ){
				throw new DaoException("插入SQL的返回类型必须为int类型");
			}
			// 3.获取输入参数
			Object bean = null;
			Object[] objects = item.getArgs();
			if( objects == null ){
				// 没有输入参数时,默认入参为void
				bean = Void.TYPE;
			}else if( objects.length == 1){
				// 一个输入参数时,检查注解,若不存在注解,则该输入参数即为bean
				SqlParameter sqlParameter = item.getMethod().getParameters()[0].getAnnotation(SqlParameter.class);
				if( sqlParameter == null ){
					bean = objects[0];
				}else{
					Map<String,Object> beanMap = new HashMap<>();
					beanMap.put(sqlParameter.value(), objects[0]);
					bean = beanMap;
				}
			}else{
				Map<String,Object> beanMap = new HashMap<>();
				// 多个参数时,检查注解
				Parameter[] parameters = item.getMethod().getParameters();
				int length = parameters.length;
				Parameter parameter = null;
				SqlParameter sqlParameter = null;
				String sqlParmName = null;
				for( int i = 0;i < length;i++ ){
					parameter = parameters[i];
					sqlParameter = parameter.getAnnotation(SqlParameter.class);
					if( sqlParameter != null ){
						sqlParmName = sqlParameter.value();
						beanMap.put(sqlParmName, objects[i]);
					}
				}
				// 子类转父类
				bean = beanMap;
			}
			// 4.拦截处理
			IntercepterSqlInfo sqlInfo = new IntercepterSqlInfo(sql,bean);
			SqlProcessIntercepter<Update> selectIntercepter = MybatisManager.getSqlProcessIntercepter(Update.class);
			// 前置拦截
			if( selectIntercepter != null ){
				selectIntercepter.beforeIntercepter( sqlInfo );
			}				
			// 5.返回结果
			Object result = SqlSession.executeUpdateSql(sql, bean,item.getDataSourceManager());
			// 后置拦截
			if( selectIntercepter != null ){
				selectIntercepter.afterIntercepter( result );
			}			
			return result;
		}

		@Override
		public void handlerException(Throwable throwable) {
			if( throwable instanceof DaoException){
				throw (DaoException)throwable;
			}
			throw new DaoException("执行更新SQL时出现异常",throwable);			
		}
	}
	
	public static class DeleteSqlProcess implements SqlProcess<Delete>,SqlExceptionProcess {
		@Override
		public Object process(Invocation<Delete> item) {
			// 1.获取原始SQL
			String sql = item.getAnno().value();
			// 2.获取返回类型
			Class<?> resultClass = item.getMethod().getReturnType();
			if( !Integer.class.isAssignableFrom(resultClass) && !int.class.isAssignableFrom(resultClass) ){
				throw new DaoException("删除sql的返回类型有误,应当为Int类型");
			}
			// 3.获取输入参数
			Object bean = null;
			Object[] objects = item.getArgs();
			if( objects.length ==  0){
				// 没有输入参数时,默认入参为void
				bean = Void.TYPE;
			}else if( objects.length == 1){
				// 一个输入参数时,检查注解,若不存在注解,则该输入参数即为bean
				SqlParameter sqlParameter = item.getMethod().getParameters()[0].getAnnotation(SqlParameter.class);
				if( sqlParameter == null ){
					bean = objects[0];
				}else{
					Map<String,Object> beanMap = new HashMap<>();
					beanMap.put(sqlParameter.value(), objects[0]);
					bean = beanMap;
				}
			}else{
				Map<String,Object> beanMap = new HashMap<>();
				// 多个参数时,检查注解
				Parameter[] parameters = item.getMethod().getParameters();
				int length = parameters.length;
				Parameter parameter = null;
				SqlParameter sqlParameter = null;
				String sqlParmName = null;
				for( int i = 0;i < length;i++ ){
					parameter = parameters[i];
					sqlParameter = parameter.getAnnotation(SqlParameter.class);
					if( sqlParameter != null ){
						sqlParmName = sqlParameter.value();
						beanMap.put(sqlParmName, objects[i]);
					}
				}
				// 子类转父类
				bean = beanMap;
			}
			// 4.拦截处理
			IntercepterSqlInfo sqlInfo = new IntercepterSqlInfo(sql,bean);
			SqlProcessIntercepter<Update> selectIntercepter = MybatisManager.getSqlProcessIntercepter(Update.class);
			// 前置拦截
			if( selectIntercepter != null ){
				selectIntercepter.beforeIntercepter( sqlInfo );
			}				
			// 5.返回结果
			Object result = SqlSession.executeDeleteSql(sql, bean,item.getDataSourceManager());
			// 后置拦截
			if( selectIntercepter != null ){
				selectIntercepter.afterIntercepter( result );
			}					
			return result;			
		}

		@Override
		public void handlerException(Throwable throwable) {
			if( throwable instanceof DaoException){
				throw (DaoException)throwable;
			}
			throw new DaoException("执行删除SQL时出现异常",throwable);			
		}
	}
}
