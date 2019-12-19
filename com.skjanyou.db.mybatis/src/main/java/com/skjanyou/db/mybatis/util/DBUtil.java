package com.skjanyou.db.mybatis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	
	public void executeSql( String sql ) {
		
	}
}
