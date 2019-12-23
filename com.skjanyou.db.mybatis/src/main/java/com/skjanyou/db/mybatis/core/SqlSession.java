package com.skjanyou.db.mybatis.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.db.mybatis.util.DBUtil;
import com.skjanyou.db.mybatis.util.StringUtil;

public class SqlSession {
	@SuppressWarnings("unchecked")
	public static<T> T getMapper( Class<T> clazz ){
		return (T) Proxy.newProxyInstance(SqlSession.class.getClassLoader(), 
				new Class[]{ clazz }, new MapperHandler(clazz));
	}
	
	public static<T,V>  V executeSelectSql( String sql, T bean, Class<V> resultClass  ){
		V result = null;
		// 1.获取填充符的位置
		Map<String,Integer> fieldMap = StringUtil.getWird( sql );
		// 2.将填充符转化为？
		String prepareSql = new String(sql);
		Set<String> sets = fieldMap.keySet();
		for (String set : sets) {
			String field = MessageFormat.format("#{0}#", set);
			prepareSql = prepareSql.replaceFirst(field, "?");
		}
		System.out.println("处理后的sql:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = DBUtil.get().getConnection().prepareStatement(prepareSql);
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
			System.out.println("executeSql:" + statement.toString());
			ResultSetMetaData metaData = rs.getMetaData();
			List<String> metaList = DBUtil.getMetaData(metaData);
			
			
			result = resultClass.newInstance();
			while( rs.next() ){
				for( String metaName : metaList ){
					Object value = rs.getObject(metaName);
					String setter = "set" + StringUtil.converFirstUpperCase(metaName);
					Method setterMethod = resultClass.getDeclaredMethod( setter ,value.getClass() );
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
		}
		
		return result;
	}
	
	public static<T> int executeDeleteSql( String sql, T bean ) {
		int resultCount = 0;
		// 1.获取填充符的位置
		Map<String,Integer> fieldMap = StringUtil.getWird( sql );
		// 2.将填充符转化为？
		String prepareSql = new String(sql);
		Set<String> sets = fieldMap.keySet();
		for (String set : sets) {
			String field = MessageFormat.format("#{0}#", set);
			prepareSql = prepareSql.replaceFirst(field, "?");
		}
		System.out.println("处理后的sql:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = DBUtil.get().getConnection().prepareStatement(prepareSql);
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
			resultCount = statement.executeUpdate();
			System.out.println("executeSql:" + statement.toString());

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
		}
				
		return resultCount;		
	}
	
	public static <T> int executeInsertSql( String sql, T bean ){
		int resultCount = 0;
		// 1.获取填充符的位置
		Map<String,Integer> fieldMap = StringUtil.getWird( sql );
		// 2.将填充符转化为？
		String prepareSql = new String(sql);
		Set<String> sets = fieldMap.keySet();
		for (String set : sets) {
			String field = MessageFormat.format("#{0}#", set);
			prepareSql = prepareSql.replaceFirst(field, "?");
		}
		System.out.println("处理后的sql:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = DBUtil.get().getConnection().prepareStatement(prepareSql);
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
			resultCount = statement.executeUpdate();
			System.out.println("executeSql:" + statement.toString());

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
		}
				
		return resultCount;
	}
	
	public static<T> int executeUpdateSql( String sql, T bean ){
		int resultCount = 0;
		// 1.获取填充符的位置
		Map<String,Integer> fieldMap = StringUtil.getWird( sql );
		// 2.将填充符转化为？
		String prepareSql = new String(sql);
		Set<String> sets = fieldMap.keySet();
		for (String set : sets) {
			String field = MessageFormat.format("#{0}#", set);
			prepareSql = prepareSql.replaceFirst(field, "?");
		}
		System.out.println("处理后的sql:" + prepareSql);

		// 3.创建PreparedStatement对象，填充参数
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = DBUtil.get().getConnection().prepareStatement(prepareSql);
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
			resultCount = statement.executeUpdate();
			System.out.println("executeSql:" + statement.toString());

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
		}
				
		return resultCount;
	}	
}
