package com.skjanyou.db.pool.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.skjanyou.db.pool.DataSource;

public class DefaultDataSource implements DataSource {
	private Connection connection;
	public DefaultDataSource( Connection connection ){
		this.connection = connection;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public DataSource commit() throws SQLException{
		this.connection.commit();
		return this;
	}

	@Override
	public DataSource rollback() throws SQLException{
		this.connection.rollback();
		return this;
	}

	@Override
	public DataSource beginTransaction() throws SQLException {
		this.connection.setAutoCommit(false);
		return this;
	}

}
