package com.skjanyou.db.bean;

import com.skjanyou.util.StringUtil;

public enum DatabaseEnum {
	ORACLE("Oracle数据库"),MYSQL("Mysql数据库"),SQLITE("SQITE数据库"),DB2("db2数据库"),
	SYBASE("sybase数据库"),HSQL("hsql数据库"),UNKNOW("未知数据库");
	private String title;
	DatabaseEnum(String title) {
		this.title = title;
	}
	
	
	public String getTitle() {
		return title;
	}


	public static DatabaseEnum resolveDbType( String url ) {
		if( StringUtil.isBlank(url) ) {
			return UNKNOW;
		}
		if( url.contains("mysql") ) {
			return MYSQL;
		}
		if( url.contains("oracle") ) {
			return ORACLE;
		}
		if( url.contains("sqlite") ) {
			return SQLITE;
		}
		if( url.contains("sybase") ) {
			return SYBASE;
		}
		if( url.contains("hsqldb") ) {
			return HSQL;
		}
		if( url.contains("db2") ) {
			return DB2;
		}
		
		return UNKNOW;
	}
}
