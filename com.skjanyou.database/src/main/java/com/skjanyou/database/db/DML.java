
package com.skjanyou.database.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skjanyou.database.para.dml.delete.DeletePara;
import com.skjanyou.database.para.dml.select.SelectPara;
import com.skjanyou.database.para.dml.update.UpdatePara;


/**数据库操作语言**/
/**
 * DML操作 
 * 2016年3月1日19:28:31
 **/
public interface DML{
    /**通过sql语句进行插入数据**/
    public boolean insertBySQL(String sql) throws SQLException;
    /**通过遍历集合中的数据进行数据的插入**/
    public boolean insert(String tableName,List<Map<String,Object>> list) throws SQLException;
   
    
    /**通过sql语句进行删除数据**/
    public boolean deleteBySQL(String sql) throws SQLException;
    /**通过表名删除所有数据**/
    public boolean delete(String tableName) throws SQLException;
    /**通过参数删除指定数据**/
    @SuppressWarnings("rawtypes")
    public boolean delete(String tableName,DeletePara para) throws SQLException;
    
    /**通过sql语句进行更新数据**/
    public boolean updateBySQL(String sql) throws SQLException;
    /**通过存数更新指定数据**/
    @SuppressWarnings("rawtypes")
    public boolean update(String tableName,UpdatePara para) throws SQLException;
    
    /**通过sql语句进行查询**/
    public List<Map<String,String>> selectBySQL(String sql) throws SQLException;
    /**从tableName中找出所有项**/
    public List<Map<String,String>> select(String tableName) throws SQLException;
    /**从tableName中找出列名为columnName的项**/
    public List<Map<String,String>> select(String tableName,String[] columnName) throws SQLException;
    /**从tableName中找出列名为columnName且满足条件的的项,rule中的key表示字段，value表值**/
    public List<Map<String,String>> select(String tableName,String[] columnName,Map<String,String> rule) throws SQLException;
    /**从tableName中找出列名为columnName且满足条件的的项;rule中的key表示字段，value表值;isLike表示是否使用Like查询**/
    public List<Map<String,String>> select(String tableName,String[] columnName,Map<String,String> rule,boolean isLike) throws SQLException;
    /**通过参数在tableName中找出项**/
    @SuppressWarnings("rawtypes")
    public List<Map<String,String>> select(String tableName,String[] columnName,SelectPara para) throws SQLException;
}

