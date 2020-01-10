package com.skjanyou.db.pool;

import java.sql.Connection;
import java.sql.SQLException;


public interface DataSource {
	public Connection getConnection();
	public DataSource commit( ) throws SQLException;
	public DataSource rollback( ) throws SQLException;
	public DataSource beginTransaction( ) throws SQLException;
}
