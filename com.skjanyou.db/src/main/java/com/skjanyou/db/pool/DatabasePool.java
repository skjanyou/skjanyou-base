package com.skjanyou.db.pool;

public interface DatabasePool {
	public DataSource getDataSource() throws InterruptedException ;
	public DatabasePool releaseConnection( DataSource ds );	
}
