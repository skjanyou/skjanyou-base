package com.skjanyou.database.dao;

import java.util.List;


public interface SkjanyouDao<T> {
	
	public boolean insert(T entity);
	
	public Integer delete(T condition);
	
	public Integer update(T condition);
	
	public T detail(T condition);
	
	public List<T> query(T condition);
	
	public List<T> queryByPage(T condition,long pageNo , long pageSize);

}
