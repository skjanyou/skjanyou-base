package com.skjanyou.db.pool.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.util.DaoUtil;

public class DefaultDataSource implements DataSource {
	private Connection connection;
	private DatabaseInfo info;
	public DefaultDataSource( DatabaseInfo info ){
		this.info = info;
		this.connection = getConnection0();
	}

	private Connection getConnection0() {
		try {
			String url = this.info.getUrl();
			String user = this.info.getUser();
			String password = this.info.getPassword();
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Connection getConnection() {
		if( info.getTestOnBorrow() && !DaoUtil.isConnectionValid(this.connection,this.info.getValidationSQL()) ) {
			this.connection = getConnection0();
		}
		
		return this.connection;
	}

	@Override
	public DataSource commit() throws SQLException{
		this.getConnection().commit();
		return this;
	}

	@Override
	public DataSource rollback() throws SQLException{
		this.getConnection().rollback();
		return this;
	}

	@Override
	public DataSource beginTransaction() throws SQLException {
		this.getConnection().setAutoCommit(false);
		return this;
	}

}
