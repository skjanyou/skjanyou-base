
package com.skjanyou.database.db;

import java.sql.SQLException;

/**数据库控制语句**/
public interface DCL {
    public void grant() throws SQLException;
    public void deny() throws SQLException;
    public void revoke() throws SQLException;
}

