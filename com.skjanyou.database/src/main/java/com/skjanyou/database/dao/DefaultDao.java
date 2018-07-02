package com.skjanyou.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.skjanyou.database.db.DefaultDatabase;

public abstract class DefaultDao<T> implements SkjanyouDao<T> {
	private Connection connection = null;
	public DefaultDao(Connection connection){
		try {
			if(connection.isClosed()){
				throw new RuntimeException("db connection is closed !");
			}
			this.connection = connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DefaultDao(DefaultDatabase db){
		this( db.getConn() );
	}
	
	@Override
	public boolean insert(T entity) {
//		connection.prepareStatement(sql);
		return false;
	}
	@Override
	public Integer delete(T condition) {
		return null;
	}
	@Override
	public Integer update(T condition) {
		return null;
	}
	@Override
	public T detail(T condition) {
		return null;
	}
	@Override
	public List<T> query(T condition) {
		return null;
	}
	@Override
	public List<T> queryByPage(T condition, long start, long end) {
		return null;
	} 
	
}
