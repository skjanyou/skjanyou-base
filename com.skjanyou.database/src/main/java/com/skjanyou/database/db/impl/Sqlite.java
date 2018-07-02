
package com.skjanyou.database.db.impl;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skjanyou.database.db.DefaultDatabase;


/**sqlite数据库**/
public class Sqlite extends DefaultDatabase {
    private static final long serialVersionUID = "Sqlite".hashCode();
	/**数据库文件默认的文件名**/
    private static final String DEFAULT_FILENAME = "DefaultDatabase.db";
	/**数据库文件名，包括路径，扩展名**/
    protected String fileName = DEFAULT_FILENAME;
    {
        this.dbName = "Sqlite";
    }
    
    /**指定数据库文件，专为Sqlite设置，其他数据库设置无效，需要在create之前调用**/
    public DefaultDatabase setFilePath(String fileName){
        this.fileName = fileName;
        return this;
    }
    public Sqlite() {
        super();
    }
    
    public Sqlite(String tableName) {
        super(tableName);
    }

    public Sqlite(String tableName, List<String> columnName) {
        super(tableName, columnName);
    }

    public Sqlite(String tableName, Map<String, String> column) {
        super(tableName, column);
    }

    public Sqlite(String tableName, String[] columnName) {
        super(tableName, columnName);
    }

    @Override
    public DefaultDatabase initConn() {
        try {
            Class.forName("org.sqlite.JDBC");
            String str_con = "jdbc:sqlite:" + this.fileName;
            conn = DriverManager.getConnection(str_con);
        } catch (SQLException e) {
            throw new RuntimeException("初始化错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    
    
}

