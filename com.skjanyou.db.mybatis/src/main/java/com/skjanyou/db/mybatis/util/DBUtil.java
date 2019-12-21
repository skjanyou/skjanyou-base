package com.skjanyou.db.mybatis.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBUtil {
	private Connection conn = null;
	private boolean isInit = false;
	private static DBUtil $this = new DBUtil();
	
	public static DBUtil init( String className,String url,String user,String password ) {
		if( $this.isInit && $this.conn != null ){
			return $this;
		}
		
		try {
			Class.forName(className);
			$this.conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("创建数据库连接失败!",e);
		}
		
		return $this;
	}
	
	public Connection getConnection(){ return this.conn; }
	
	public static DBUtil get(){ return $this; }
	
	public<T,V> V executeSelectSql( String sql, T bean, Class<V> resultClass  ){
		V result = null;
		// 1.获取填充符的位置
		int length = sql.length();
		int index = 0;int size = 1;
		int startFlag = 0,endFlag = 0;
		Map<String,Integer> fieldMap = new LinkedHashMap<>();
		while( index < length ){
			char c = sql.charAt(index);
			if( c == '#' ){
				if( startFlag <= endFlag ){
					startFlag = index;
				}else{
					endFlag = index;
					String subString = sql.substring(startFlag + 1,endFlag);
					System.out.println(subString);
					fieldMap.put(subString, size++);
				}
			}
			index++;
		}
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
			statement = this.conn.prepareStatement(prepareSql);
			Class<?> targetClass = bean.getClass();
			int setIdx = 1;
			for (String field : sets) {
				// 通过反射获取bean内部的值
				String getter = "get" + converFirstUpperCase(field);
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
			List<String> metaList = getMetaData(metaData);
			
			
			result = resultClass.newInstance();
			while( rs.next() ){
				for( String metaName : metaList ){
					Object value = rs.getObject(metaName);
					String setter = "set" + converFirstUpperCase(metaName);
					Method setterMethod = resultClass.getDeclaredMethod( setter ,value.getClass() );
					setterMethod.invoke(result, value);
				}
			}

		} catch (Exception e){
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
	
	private String converFirstUpperCase(String str){
		return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
	}
	
	private List<String> getMetaData( ResultSetMetaData metaData ) throws SQLException{
		List<String> list = new ArrayList<>();
		int count = metaData.getColumnCount();
		// 下标从1开始
		for( int columnIdx = 1; columnIdx <= count;columnIdx++ ){
			String columnName = metaData.getColumnName(columnIdx);
			list.add(columnName);
		}		
		return list;
	}
}
