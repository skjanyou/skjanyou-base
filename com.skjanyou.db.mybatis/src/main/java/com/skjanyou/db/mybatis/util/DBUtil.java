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
	

	

	
	public static List<String> getMetaData( ResultSetMetaData metaData ) throws SQLException{
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
