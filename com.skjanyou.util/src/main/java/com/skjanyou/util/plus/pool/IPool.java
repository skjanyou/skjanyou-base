package com.skjanyou.util.plus.pool;

public interface IPool<T> {
	public IPool<T> initPool();
	
	public T getEntity() throws InterruptedException;
	
	public T getEntityWithoutException() ;
	
	public T getEntity( long waitTime ) throws InterruptedException;
	
	public T getEntityWithoutException( long waitTime ) ;
	
	public IPool<T> clear();
	
	public IPool<T> reset();
	
	public IPool<T> release( T entity );
}
