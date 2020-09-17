package com.skjanyou.db.pool.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.db.pool.DatabasePool;

public class DefaultDataBasePool implements DatabasePool {
	public LinkedList<DataSource> pools = new LinkedList<>();
	private DatabaseInfo info;
	public DefaultDataBasePool( DatabaseInfo info ){
		this.info = info;
		this.initDatabase();
	}

	/** 初始化数据 */
	private void initDatabase(){
		try {
			Class.forName(this.info.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/** 初始化数据库连接池 */
	public DatabasePool initDatabasePool() {
		String url = this.info.getUrl();
		String user = this.info.getUser();
		String password = this.info.getPassword();

		int size = this.info.getSize();
		try {
			DataSource ds = null;
			for( int i = 0;i < size;i++ ){
				Connection connection = DriverManager.getConnection(url, user, password);
				ds = new DefaultDataSource( connection );
				pools.addLast(ds);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return this;
	} 


	@Override
	public DataSource getDataSource() throws InterruptedException {
		// 同步pools
		synchronized (pools) {
			// 非超时等待模式
			if (info.getMills() <= 0) {
				while (pools.isEmpty()) {
					//  会把当前的锁释放，然后让出CPU，进入等待状态
					pools.wait();
				}
				// 取出链表中头数据
				return pools.removeFirst();
			} else {
				long future = System.currentTimeMillis() + info.getMills();
				long remaining = info.getMills();
				while (pools.isEmpty() && remaining > 0) {
					// 会把当前的锁释放，然后让出CPU，进入等待状态
					pools.wait(remaining);
					remaining = future - System.currentTimeMillis();
				}
				//  获取不到返回null
				return pools.isEmpty() ? null: pools.removeFirst();
			}
		}
	}

	@Override
	public DatabasePool releaseConnection(DataSource ds) {
		if (ds != null) {
			synchronized (pools) {
				// 将连接添加到链表的末尾
				pools.addLast(ds);
				// 唤醒所有线程
				pools.notifyAll();
			}
		}
		return this;
	}

}
