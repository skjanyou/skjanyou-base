package com.skjanyou.util.plus.pool;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPool<T> implements IPool<T> {
	protected volatile LinkedList<T> pool = new LinkedList<>();
	
	@Override
	public final IPool<T> initPool() {
		this.clear();
		this.reset();
		return this;
	}

	@Override
	public final T getEntity() throws InterruptedException {
		synchronized ( this.pool ) {
			while( this.pool.isEmpty() ) {
				this.pool.wait();
			}
			return this.pool.removeFirst();
		}
	}

	@Override
	public final T getEntityWithoutException() {
		T result = null;
		try {
			result = this.getEntity();
		} catch (InterruptedException e) {}
		return result;
	}

	@Override
	public final T getEntity(long waitTime) throws InterruptedException {
		long future = System.currentTimeMillis() + waitTime;
		long remaining = waitTime;
		synchronized ( this.pool ) {
			InterruptedException exception = null;
			while( this.pool.isEmpty() && remaining > 0 ) {
				try {
					exception = null;
					this.pool.wait(waitTime);
				} catch( InterruptedException e ) {
					exception = e;
				}
				remaining = future - System.currentTimeMillis();
			}
			if( exception != null ) {
				throw exception;
			}
			
			return this.pool == null ? null : this.pool.removeFirst();
		}
		
	}

	@Override
	public final T getEntityWithoutException(long waitTime) {
		T result = null;
		try {
			result = this.getEntity(waitTime);
		} catch (InterruptedException e) {}
		return result;
	}

	@Override
	public final IPool<T> clear() {
		for (T entity : pool) {
			if( entity != null ) {
				try {
					this.close(entity);
				} catch (Exception e) {}
			}
		}
		pool.clear();
		return this;
	}

	@Override
	public final IPool<T> reset() {
		pool.addAll(pullEntityToPool());
		return this;
	}

	@Override
	public final IPool<T> release(T entity) {
		if( entity != null ) {
			synchronized ( this.pool ) {
				this.pool.addLast(entity);
				this.pool.notifyAll();
			}
		}
		
		return this;
	}

	// 获取方法
	public abstract List<T> pullEntityToPool() ;
	// 释放方法
	public abstract void close( T entity ) throws Exception ;
}
