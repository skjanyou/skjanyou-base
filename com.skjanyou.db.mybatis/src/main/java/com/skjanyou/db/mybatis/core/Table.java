package com.skjanyou.db.mybatis.core;

public interface Table {
	public static interface Insert<T> extends Executeable<T>{}
	public static interface Delete<T> extends Executeable<T>{}
	public static interface Update<T> extends Executeable<T>{}
	public static interface Select<T> extends Executeable<T>{}
	public static interface Column<T> extends Executeable<T>{}
	public static interface Where<T> extends Executeable<T>{}
	public static interface Executeable<T>{
		public T execute();
	}
	
	
	public Insert insert();
	public Delete delete();
	public Update update();
	public Select select();
}
