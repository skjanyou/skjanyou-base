
package com.skjanyou.database.db.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.skjanyou.database.db.DefaultDatabase;

public class DB2 extends DefaultDatabase {
    private static final long serialVersionUID = "DB2".hashCode();
    private String driver = null;
    private String username = null;
    private String password = null;
    private String url = null;
    {
        this.dbName = "DB2";
    }
    
    
    public DB2(String driver, String username, String password, String url) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.url = url;
    }



    @Override
    public DefaultDatabase initConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

}

