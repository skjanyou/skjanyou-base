
package com.skjanyou.database.db;

import java.sql.SQLException;

import com.skjanyou.database.para.ddl.alter.AlterPara;


/**数据库定义语句**/
/**
 * DDL操作 
 * 2016年3月2日9:05:54
 **/
public interface DDL {
	public boolean createBySQL(String sql) throws SQLException;
    public boolean create() throws SQLException;
    
    public boolean alterBySQL(String sql) throws SQLException;
    public boolean alter(String tableName,AlterPara para) throws SQLException;
    public boolean alter(AlterPara para) throws SQLException;
    public boolean alter(String tableName,String method,String[] columnName) throws SQLException;
    public boolean alter(String method,String[] columnName) throws SQLException;
    
    
    public boolean dropBySQL(String sql) throws SQLException;
    public boolean drop(String tableName) throws SQLException;
    public boolean drop() throws SQLException;
}

