package com.skjanyou.db.mybatis.test;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.util.DataSourceManager;

import junit.framework.TestCase;

public class UserTest extends TestCase {
	protected DataSourceManager defaultDataSourceManager = null;
	{
    	String className = "com.mysql.cj.jdbc.Driver";
    	String url = "jdbc:mysql://127.0.0.1:3306/blog?zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    	String user = "root";
    	String password = "123456";
    	DatabaseInfo info = new DatabaseInfo(className,url,user,password,20,2000);
    	defaultDataSourceManager = new DataSourceManager(info);
	}
	
}
