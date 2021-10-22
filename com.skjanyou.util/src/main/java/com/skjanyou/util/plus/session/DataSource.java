package com.skjanyou.util.plus.session;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
	// 获取当前线程的数据库连接
	public Connection getConnection();
	// 开始事务
	public DataSource beginTransaction() throws SQLException;
	// 提交事务
	public DataSource commit() throws SQLException;
	// 回滚事务
	public DataSource rollback() throws SQLException;
	// 释放连接
	public void release( Connection connection );
}
