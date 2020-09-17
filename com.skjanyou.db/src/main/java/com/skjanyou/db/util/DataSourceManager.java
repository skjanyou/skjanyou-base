package com.skjanyou.db.util;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.pool.DatabasePool;
import com.skjanyou.db.pool.impl.DefaultDataBasePool;

public class DataSourceManager {
	private DatabasePool pools = null;
	public ThreadLocal<DataSource> currentDs = new ThreadLocal<>();
	public DataSourceManager( DatabaseInfo info){ 
		pools = new DefaultDataBasePool(info);
		pools.initDatabasePool();
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
	
	public DataSourceManager releaseConnection( DataSource ds ){
		currentDs.remove();
		pools.releaseConnection(ds);
		return this;
	}
	
	

}
