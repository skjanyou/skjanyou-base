package com.skjanyou.db.mybatis.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.bean.MybatisSqlExecuteCache;
import com.skjanyou.db.mybatis.util.SqlUtil;
import com.skjanyou.db.mybatis.util.StringUtil;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.util.DaoUtil;
import com.skjanyou.db.util.DataSourceManager;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.util.BeanWrapper;

public class SqlSession {
	private static Logger logger = LogUtil.getLogger(SqlSession.class);	
	private static Map<String,MybatisSqlExecuteCache> sqlExecuteCache = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static<T> T getMapper( Class<T> clazz, DataSourceManager dataSourceManager ){
		if(clazz.getAnnotation(Mapper.class) == null){ throw new RuntimeException("Mapper类必须添加@Mapper注解"); }
		return (T) Proxy.newProxyInstance(SqlSession.class.getClassLoader(), 
				new Class[]{ clazz }, new MapperHandler(clazz,dataSourceManager));
	}
	
	
	private static MybatisSqlExecuteCache getSqlExecuteCache( String sql ){
		String prepareSql = null;
		Set<String> sets = null;
		// 1.从缓存中查询是否已经有进行编译
		MybatisSqlExecuteCache sqlCache = sqlExecuteCache.get(sql);
		if( sqlCache != null ){
			return sqlCache;
		}
		// 2.否则计算
		synchronized ( sqlExecuteCache ) {
			sqlCache = sqlExecuteCache.get(sql);
			if( sqlCache != null ) {
				return sqlCache;
			}
			// 1.1获取填充符的位置
			Map<String,Integer> fieldMap = StringUtil.getWird( sql );
			// 1.2将填充符转化为？
			prepareSql = new String(sql);
			sets = fieldMap.keySet();
			for (String set : sets) {
				String field = MessageFormat.format("#{0}#", set);
				prepareSql = prepareSql.replaceFirst(field, "?");
			}
			sqlCache = new MybatisSqlExecuteCache(sql, prepareSql, sets);
			sqlExecuteCache.put(sql, sqlCache);
		}
		return sqlCache;
	}
	
	public static<T,V>  V executeSelectSql( String sql, T bean, Class<V> resultClass,DataSourceManager dataSourceManager  ){
		DataSource ds = dataSourceManager.getDataSource();
		V result = null;
		MybatisSqlExecuteCache sqlCache = getSqlExecuteCache( sql );
		String prepareSql = sqlCache.getPrepareSql();
		Set<String> sets = sqlCache.getSets();
		logger.debug("处理后的SQL:" + prepareSql);
		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = ds.getConnection().prepareStatement(prepareSql);
			Class<?> targetClass = bean.getClass();
			int setIdx = 1;
			for (String field : sets) {
				// 通过反射获取bean内部的值
				String getter = "get" + StringUtil.converFirstUpperCase(field);
				Method method = targetClass.getDeclaredMethod(getter);
				Object fieldObj = method.invoke(bean, new Object[]{});
				
				// 填充参数
				statement.setObject(setIdx, fieldObj);
				setIdx++;
			}
			
			// 执行查询sql
			rs = statement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			List<String> metaList = DaoUtil.getMetaData(metaData);
			
			
			result = resultClass.newInstance();
			while( rs.next() ){
				for( String metaName : metaList ){
					Object value = rs.getObject(metaName);
					String setter = "set" + StringUtil.converFirstUpperCase(metaName);
					Method setterMethod = resultClass.getDeclaredMethod( setter ,value.getClass() );
					if( value instanceof Clob ) {
						value = SqlUtil.clobConvertToString( (Clob)value );
					}					
					setterMethod.invoke(result, value);
				}
			}

		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("查询失败",e);
		} finally {
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( statement != null ){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if( ds.getConnection().getAutoCommit() ) {
					dataSourceManager.releaseConnection(ds);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static<T,V>  List<V> executeSelectListSql( String sql, T bean, Class<V> resultClass,DataSourceManager dataSourceManager  ){
		DataSource ds = dataSourceManager.getDataSource();
		List<V> result = new ArrayList<>();
		MybatisSqlExecuteCache sqlCache = getSqlExecuteCache( sql );
		String prepareSql = sqlCache.getPrepareSql();
		Set<String> sets = sqlCache.getSets();
		logger.debug("处理后的SQL:" + prepareSql);
		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = ds.getConnection().prepareStatement(prepareSql);
			BeanWrapper beanWrapper = new BeanWrapper(bean);
			int setIdx = 1;
			for (String field : sets) {
				Object setObj = null;
				if( Map.class.isAssignableFrom(beanWrapper.getTargetClass()) ){
					// Map接口通过get函数获取
					Method getMethod = beanWrapper.getTargetClass().getDeclaredMethod("get",Object.class);
					setObj = getMethod.invoke(bean, field);
				}else{
					// 通过反射获取bean内部的值
					setObj = beanWrapper.get(field);
				}
				
				// 填充参数
				statement.setObject(setIdx, setObj);
				setIdx++;
			}
			
			// 执行查询sql
			rs = statement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			List<String> metaList = DaoUtil.getMetaData(metaData);
			
			
			while( rs.next() ){
				V resultBean = resultClass.newInstance();
				BeanWrapper resultBeanWrapper = new BeanWrapper(resultBean);
				for( String metaName : metaList ){
					Object value = rs.getObject(metaName);
					if( value instanceof Clob ) {
						value = SqlUtil.clobConvertToString( (Clob)value );
					}
					resultBeanWrapper.set(metaName,value);
				}
				result.add(resultBean);
			}

		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("查询失败",e);
		} finally {
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( statement != null ){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if( ds.getConnection().getAutoCommit() ) {
					dataSourceManager.releaseConnection(ds);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}	
	
	public static<T> int executeDeleteSql( String sql, T bean,DataSourceManager dataSourceManager ) {
		DataSource ds = dataSourceManager.getDataSource();
		int resultCount = 0;
		MybatisSqlExecuteCache sqlCache = getSqlExecuteCache( sql );
		String prepareSql = sqlCache.getPrepareSql();
		Set<String> sets = sqlCache.getSets();
		logger.debug("处理后的SQL:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = ds.getConnection().prepareStatement(prepareSql);
			Class<?> targetClass = bean.getClass();
			int setIdx = 1;
			for (String field : sets) {
				Object setObj = null;
				if( Map.class.isAssignableFrom(targetClass) ){
					// Map接口通过get函数获取
					Method getMethod = targetClass.getDeclaredMethod("get",Object.class);
					setObj = getMethod.invoke(bean, field);
				}else{
					// 通过反射获取bean内部的值
					String getter = "get" + StringUtil.converFirstUpperCase(field);
					Method method = targetClass.getDeclaredMethod(getter);
					setObj = method.invoke(bean, new Object[]{});
				}
				
				// 填充参数
				statement.setObject(setIdx, setObj);
				setIdx++;
			}
			
			// 执行查询sql
			resultCount = statement.executeUpdate();

		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("删除失败",e);
		} finally {
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( statement != null ){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if( ds.getConnection().getAutoCommit() ) {
					dataSourceManager.releaseConnection(ds);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return resultCount;		
	}
	
	public static <T> int executeInsertSql( String sql, T bean,DataSourceManager dataSourceManager ){
		DataSource ds = dataSourceManager.getDataSource();
		int resultCount = 0;
		MybatisSqlExecuteCache sqlCache = getSqlExecuteCache( sql );
		String prepareSql = sqlCache.getPrepareSql();
		Set<String> sets = sqlCache.getSets();
		logger.debug("处理后的SQL:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = ds.getConnection().prepareStatement(prepareSql);
			Class<?> targetClass = bean.getClass();
			int setIdx = 1;
			for (String field : sets) {
				Object setObj = null;
				if( Map.class.isAssignableFrom(targetClass) ){
					// Map接口通过get函数获取
					Method getMethod = targetClass.getDeclaredMethod("get",Object.class);
					setObj = getMethod.invoke(bean, field);
				}else{
					// 通过反射获取bean内部的值
					String getter = "get" + StringUtil.converFirstUpperCase(field);
					Method method = targetClass.getDeclaredMethod(getter);
					setObj = method.invoke(bean, new Object[]{});
				}

				
				// 填充参数
				statement.setObject(setIdx, setObj);
				setIdx++;
			}
			
			// 执行查询sql
			resultCount = statement.executeUpdate();

		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("插入失败",e);
		} finally {
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( statement != null ){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if( ds.getConnection().getAutoCommit() ) {
					dataSourceManager.releaseConnection(ds);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return resultCount;
	}
	
	public static<T> int executeUpdateSql( String sql, T bean,DataSourceManager dataSourceManager ){
		DataSource ds = dataSourceManager.getDataSource();
		int resultCount = 0;
		MybatisSqlExecuteCache sqlCache = getSqlExecuteCache( sql );
		String prepareSql = sqlCache.getPrepareSql();
		Set<String> sets = sqlCache.getSets();
		logger.debug("处理后的SQL:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = ds.getConnection().prepareStatement(prepareSql);
			Class<?> targetClass = bean.getClass();
			int setIdx = 1;
			for (String field : sets) {
				Object setObj = null;
				if( Map.class.isAssignableFrom(targetClass) ){
					// Map接口通过get函数获取
					Method getMethod = targetClass.getDeclaredMethod("get",Object.class);
					setObj = getMethod.invoke(bean, field);
				}else{
					// 通过反射获取bean内部的值
					String getter = "get" + StringUtil.converFirstUpperCase(field);
					Method method = targetClass.getDeclaredMethod(getter);
					setObj = method.invoke(bean, new Object[]{});
				}
				
				// 填充参数
				statement.setObject(setIdx, setObj);
				setIdx++;
			}
			
			// 执行查询sql
			resultCount = statement.executeUpdate();

		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("更新失败",e);
		} finally {
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( statement != null ){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if( ds.getConnection().getAutoCommit() ) {
					dataSourceManager.releaseConnection(ds);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return resultCount;
	}	
}
