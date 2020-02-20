package com.skjanyou.db.mybatis.bean;

import java.util.Set;

public class MybatisSqlExecuteCache {
	// 原始sql
	private String sql;
	// 预编译sql
	private String prepareSql;
	// 字段
	private Set<String> sets;
	
	public MybatisSqlExecuteCache(){}
	public MybatisSqlExecuteCache(String sql, String prepareSql,
			Set<String> sets) {
		super();
		this.sql = sql;
		this.prepareSql = prepareSql;
		this.sets = sets;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getPrepareSql() {
		return prepareSql;
	}
	public void setPrepareSql(String prepareSql) {
		this.prepareSql = prepareSql;
	}
	public Set<String> getSets() {
		return sets;
	}
	public void setSets(Set<String> sets) {
		this.sets = sets;
	}
}
