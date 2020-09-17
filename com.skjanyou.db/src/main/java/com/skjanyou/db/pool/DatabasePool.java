package com.skjanyou.db.pool;

public interface DatabasePool {
	public DatabasePool initDatabasePool();
	public DataSource getDataSource() throws InterruptedException ;
	public DatabasePool releaseConnection( DataSource ds );	
}
