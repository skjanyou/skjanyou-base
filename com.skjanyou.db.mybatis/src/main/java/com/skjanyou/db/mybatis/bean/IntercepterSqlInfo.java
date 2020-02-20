package com.skjanyou.db.mybatis.bean;

public class IntercepterSqlInfo {
	private String sql;
	private Object bean;
	
	public IntercepterSqlInfo(String sql, Object bean) {
		super();
		this.sql = sql;
		this.bean = bean;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
}
