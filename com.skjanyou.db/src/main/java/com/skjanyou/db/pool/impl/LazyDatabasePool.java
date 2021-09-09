package com.skjanyou.db.pool.impl;


import java.util.ArrayList;
import java.util.List;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.pool.DatabasePool;

public class LazyDatabasePool extends DefaultDataBasePool {
	// 数据源可创建的最大值
	private int size;
	// 暂存数据库配置信息
	private DatabaseInfo info;
	// 保存该线程池创建的所有数据源
	private volatile List<DataSource> selfDataSource = new ArrayList<>();

	public LazyDatabasePool(DatabaseInfo info) {
		super(info);
		this.info = info;
		this.size = info.getSize();
	}

	@Override
	public DatabasePool initDatabasePool() {
		return this;
	}

	protected DataSource createDataSource() {
		return new DefaultDataSource(this.info);
	}
	

	@Override
	public DataSource getDataSource() throws InterruptedException {
		// 数据源全部都已经创建完成
		if( this.selfDataSource.size() == this.size ) {
			return super.getDataSource();
		}
		
		// 数据源未全部创建,但是仅池里面有可用的数据源
		synchronized( this.pools ) {
			if( this.pools.size() > 0 ) {
				// 获取到了就返回
				return this.pools.removeFirst();
			}
		}
		
		// 池未达阈值，再次创建
		synchronized (this.selfDataSource) {
			DataSource ds = createDataSource();
			this.selfDataSource.add(ds);
			return ds;
		}
	}

	@Override
	public DatabasePool releaseConnection(DataSource ds) {
		if (ds != null) {
			if( !this.selfDataSource.contains(ds) ) { throw new RuntimeException("数据源" + ds + "非该数据库连接池产生，无法使用该池释放。"); } 
			synchronized (this.pools) {
				this.pools.addLast(ds);
				this.pools.notifyAll();
			}
		}
		return this;
	}
}
