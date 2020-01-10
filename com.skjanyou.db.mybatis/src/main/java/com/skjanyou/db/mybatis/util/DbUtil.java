package com.skjanyou.db.mybatis.util;

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
	private DbUtil(){}
	public static DbUtil init( DatabaseInfo info ){
		pools = new DefaultDataBasePool(info);
		return $this;
	}
	
	public DataSource getDataSource(){
		try {
			return pools.getDataSource();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static DbUtil get(){
		return $this;
	}
	
	public DbUtil releaseConnection( DataSource ds){
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
