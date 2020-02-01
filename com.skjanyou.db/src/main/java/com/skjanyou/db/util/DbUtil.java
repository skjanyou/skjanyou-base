package com.skjanyou.db.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.pool.DatabasePool;
import com.skjanyou.db.pool.impl.DefaultDataBasePool;

public class DbUtil {
	private static DatabasePool pools = null;
	private static DbUtil $this = new DbUtil();
	public ThreadLocal<DataSource> currentDs = new ThreadLocal<>();
	private DbUtil(){}
	public static DbUtil init( DatabaseInfo info ){
		pools = new DefaultDataBasePool(info);
		return $this;
	}
	
	public synchronized DataSource getDataSource(){
		try {
			// 保证一个线程只有一个连接
			DataSource ds = currentDs.get();
			if( ds == null){
				ds = pools.getDataSource();
				currentDs.set(ds);
			}
			return ds;
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static DbUtil get(){
		return $this;
	}
	
	public DbUtil releaseConnection( DataSource ds){
		currentDs.remove();
		pools.releaseConnection(ds);
		return $this;
	}
	
	
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
