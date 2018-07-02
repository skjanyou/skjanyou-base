
package com.skjanyou.database.db;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.database.para.comm.ColumnType;
import com.skjanyou.database.para.ddl.alter.AlterPara;
import com.skjanyou.database.para.dml.delete.DeletePara;
import com.skjanyou.database.para.dml.delete.DeleteParaItem;
import com.skjanyou.database.para.dml.select.SelectPara;
import com.skjanyou.database.para.dml.select.SelectParaItem;
import com.skjanyou.database.para.dml.update.UpdatePara;
import com.skjanyou.database.para.dml.update.UpdateParaItem;


/**
 * 作者：skjanyou
 * 作用：快速建立表、对表进行操作，进行表的增删改查，事务控制
 * 扩展：
 * 时间：2016年2月29日20:06:11
 * Version：1
 * */
public abstract class DefaultDatabase implements Serializable,Cloneable,Closeable,DML,DDL,DCL{
    private static final long serialVersionUID = "DefaultDatabase".hashCode();
    /**默认使用的字段**/
    private static final ColumnType DEFAULT_TYPE = ColumnType.VARCHAR;
    /**默认使用字段的大小**/
    private static final int DEFAULT_SIZE = 20;
    /**使用的数据库名字**/
    protected String dbName = "DefaultDatabase";
    /**与该数据库的链接**/
    protected Connection conn = null;
    /**要建立的表的名字**/
    protected String tableName = null;
    /**列名，不指定列类型，使用默认的列类型**/
    protected String[] columnName = null;
    /**不指定列类型时，VARCHAR类型的长度**/
    protected int column_size = DEFAULT_SIZE;
    /**列名及列类型，不适用默认列类型**/
    protected Map<String,String> map = null;
    
   
    /**使用这种方法建表，默认全部为VARCHAR类型,且需要使用setTableName设定表名、使用setColumnName或setColumnNT设置表字段，否则报错**/
    public DefaultDatabase(){
        
    }
    /**使用这种方法建表，默认全部为VARCHAR类型,且需要使用setColumnName或setColumnNT设置表字段，否则报错**/
    public DefaultDatabase(String tableName){
        this.tableName = tableName;
    }
    /**使用这种方法建表，默认全部为VARCHAR类型**/
    public DefaultDatabase(String tableName, String[] columnName){
        this.tableName = tableName;
        this.columnName = columnName;
    }
    /**使用这种方法建表，默认全部为VARCHAR类型**/
    public DefaultDatabase(String tableName,List<String> columnName){
        String[] tempArr = new String[columnName.size()];
        for(int i = 0;i < columnName.size();i++){
            tempArr[i] = columnName.get(i);
        }
        this.tableName = tableName;
        this.columnName = tempArr;
    }
    /**使用这种方法建表，可以指定列的名字以及类型**/
    public DefaultDatabase(String tableName,Map<String,String> column){
        this.tableName = tableName;
        this.map = column;
    }
    
    /**在外部调用create之前使用时，可以改变数据库，初始化数据库链接,重写此方法可以改变数据库连接,需要指定数据库驱动以及获得链接
     * 例如：
     * Class.forName("org.sqlite.JDBC");
     * String str_con = "jdbc:sqlite:" + this.fileName;
     * conn = DriverManager.getConnection(str_con);
     * 即可获得sqlite的数据库链接
     * **/
    public abstract DefaultDatabase initConn();
    
    /**指定表名字**/
    public DefaultDatabase setTableName(String tableName){
        this.tableName = tableName;
        return this;
    }
    /**指定字段的长度，当没有指定字段类型时，使用这个方法则可以改变VARCHAR的长度**/
    public DefaultDatabase setColumn_size(int column_size) throws SQLException{
        if(column_size <= 0){throw new SQLException("给定的字段长度有误，无法小于或等于零");}
        this.column_size = column_size;
        return this;
    }
    
    /**设定表列的名字**/
    public DefaultDatabase setColumnName(String[] columnName){
        this.columnName = columnName;
        return this;
    }
    /**设定表列的名字，及类型**/
    public DefaultDatabase setColumnTN(Map<String,String> map){
        this.map = map;
        return this;
    }
    
    /**获得数据库链接**/
    public Connection getConn() {
        return conn;
    }
    
    /**设定数据库链接，可以给定其他数据库的链接，但是不要再使用initConn()方法**/
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    /**使用这个方法，不仅数据库链接被释放，而且类内部数据将重置**/
    public DefaultDatabase reset() throws IOException{
        close();
        conn = null;
        tableName = null;
        columnName = null;
        column_size = DEFAULT_SIZE;
        map = null;
        return this;
    }
    
    
    
    
    /*
     * 以下全部为实现接口的方法
     * 
     * */
    /*
     * DDL语句
     * 包括create、alter、drop
     * */
    @Override
    public boolean createBySQL(String sql) throws SQLException {
    	boolean result = false;
    	 //sql语句构建完成
        System.out.println("createBySQL:[" + sql + "]");
        //开始建表
        int num = 0;
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            num = statement.executeUpdate();
            statement.close();
            conn.commit();
        }catch(SQLException e){
            num = -1;
            conn.rollback();
        }finally{
            conn.setAutoCommit(true);
        }
        if(num == -1){
            result = false;
            System.out.println("create:表[" + tableName + "]表存在");
        }else{
            result = true;
            System.out.println("create:表[" + tableName + "]建立成功");
        }
    	return result;
    }
    /**开始建表,initConn方法必须位于此方法前面，并且使用此方法之后无法继续使用initConn方法，否则报错**/
    @Override
    public boolean create() throws SQLException{
    	String sql= "create table " + tableName + "(";
        //没有指定行类型
        if(map == null){
            if(columnName != null && columnName.length > 0){
                for(int i = 0;i < columnName.length;i++){
                    sql = sql + columnName[i] + " " + DEFAULT_TYPE + "(" + column_size + ")";
                    if(i == columnName.length - 1){
                        sql+=")";
                    }else{
                        sql+=",";
                    }
                }
            }else{
                throw new SQLException("无法创建没有列的表");
            }
        }else{//指定了行类型
            Set<String> set = map.keySet();
            for (String columnName : set) {
                String type = map.get(columnName);
                if(type == null || "".equals(type)){throw new SQLException("列参数有误");}
                if(!ColumnType.isExist(type)){throw new SQLException("给出的行类型有误！");}
                sql = sql + columnName + " " + type + ",";
            }
            sql = sql.substring(0, sql.length() - 1) +  ")";
        }
        return createBySQL(sql);
    }
    /**对表字段属性等进行操作**/
    @Override
    public boolean alterBySQL(String sql) throws SQLException{
        boolean result = true;
        System.out.println("alterBySQL:[" + sql + "]");
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            conn.commit();
        }catch(SQLException e){
            result = false;
        }finally{
            conn.setAutoCommit(true);
        }
        return result;
    }
    
    @Override
    public boolean alter(String tableName,AlterPara para) throws SQLException{
        if(para == null){throw new SQLException("列操作参数有误！");}
        String sql = "alter table " + tableName + " " + para.getMethod() + " ";
        Map<String,String> map = para.getMap();
        Set<String> set = map.keySet();
        int size = set.size();
        int current = 0;
        for (String key : set) {
            String value = map.get(key);
            sql = sql + " " + key + " " + value;
            if(current < size - 1){
                sql += ",";
            }
            current++;
        }
        return alterBySQL(sql);
    };
    
    @Override
    public boolean alter(AlterPara para) throws SQLException{
        return alter(this.tableName,para);
    };
    
    @Override
    public boolean alter(String tableName,String method,String[] columnName) throws SQLException{
        String sql = "alter table " + tableName + " " + method;
        
        return alterBySQL(sql);
    }
    @Override
    public boolean alter(String method,String[] columnName) throws SQLException{
        return alter(this.tableName,method,columnName);
    }
    
    @Override
    public boolean dropBySQL(String sql) throws SQLException {
    	boolean result = true;
        System.out.println("dropBySQL:[" + sql + "]");
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            conn.commit();
        }catch(SQLException e){
            conn.rollback();
            result = false;
        }finally{
            conn.setAutoCommit(true);
        }
        return result;
    }
    /**删除表数据**/
    @Override 
    public boolean drop(String tableName) throws SQLException{
        String sql = "drop table " + tableName;
        return dropBySQL(sql);
    }
    
    /**若前面指定了表，则默认删除这个表**/
    @Override
    public boolean drop() throws SQLException{
        return drop(this.tableName);
    };
    

    /*
     * DML语句
     * insert,update,delete需要进行事务控制
     * 
     * */
    
    
    /**通过sql语句进行插入数据**/
    @Override
    public synchronized boolean insertBySQL(String sql) throws SQLException{
        boolean result = false;
        int num = 0;
        try {
            System.out.println("insertBySQL:SQL语句：[" + sql + "]");
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            num = statement.executeUpdate();
            statement.close();
            conn.commit();
        } catch (SQLException e) {
            num = 0;
            conn.rollback();
        }finally{
            conn.setAutoCommit(true);
        }
        if(num > 0){
            result = true;
        }else{
            result = false;
        }
        return result;
    }
    /**通过遍历集合中的数据进行数据的插入**/
    @Override
    public boolean insert(String tableName,List<Map<String, Object>> list) throws SQLException {
        boolean result = false;
        String sql = "insert into " + tableName + "(";
        int size = list.size();
        if(size > 0){
            for(int i = 0;i < size;i++){
                Map<String,Object> map = list.get(i);
                Set<String> set = map.keySet();
                int ssize = set.size();
                int index = 0;
                if(i == 0){
                    for (String columnName : set) {
                        sql = sql + columnName;
                        if(index < ssize - 1){
                            sql += ",";
                        }else{
                            sql +=")";
                        }
                        index++;
                    }
                    index = 0;
                    sql += " select ";
                    for(String columnName : set){
                        Object o = map.get(columnName);
                        String value = "";
                        if(o instanceof String){
                            value = "'" + o.toString() + "'";
                        }else{
                            if(o == null){
//                                value = "null";
                                value = null;
                            }else{
                                value = o.toString();  
                            }
                        }
                        sql = sql + value;
                        if(index < ssize - 1){
                            sql += ",";
                        }else{
                            sql += " ";
                        }
                        index++;
                    }
                }else{
                    sql += " union select ";
                    for(String columnName : set){
                        Object o = map.get(columnName);
                        String value = "";
                        if(o instanceof String){
                            value = "'" + o.toString() + "'";
                        }else{
                            value = o.toString(); 
                        }
                        sql = sql + value;
                        if(index < ssize - 1){
                            sql += ",";
                        }else{
                            sql += " ";
                        }
                        index++;
                    }  
                }
            }
            result = insertBySQL(sql);
        }else{
            throw new SQLException("无法插入空的数据");
        }
        return result;
    }
    
    /**通过sql语句进行删除数据**/
    @Override
    public synchronized boolean deleteBySQL(String sql) throws SQLException {
        boolean result = true;
        try{
            System.out.println("deleteBySQL:SQL语句：[" + sql + "]");
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            conn.commit();
        }catch(SQLException e){
            result = true;
            conn.rollback();
        }finally{
            conn.setAutoCommit(true);
        }
        return result;
    }
    
    /**通过表名删除所有数据**/
    @Override
    public boolean delete(String tableName) throws SQLException{
        String sql = "delete from " + tableName;
        return deleteBySQL(sql);
    }
    
    /**通过参数删除指定数据**/
    @SuppressWarnings("rawtypes")
    @Override
    public boolean delete(String tableName,DeletePara para) throws SQLException{
        String sql = "delete from " + tableName + " where 1=1 ";
        while(para.hasNext()){
            DeleteParaItem dpi = para.get();
            String columnName = dpi.getColumnName().toString();
            String value = "";
            Object o = dpi.getValue();
            if(o instanceof String){
                value = "'" + o.toString() + "'";
            }else{
                value = o.toString(); 
            }
            String method = dpi.getMethod().toString();
            String andor = dpi.getAndOr().toString();
            sql = sql + andor + " " + columnName + " " + method + " " + value + " ";
        }
        return deleteBySQL(sql);
    };
    
    /**通过sql语句进行更新数据**/
    @Override
    public synchronized boolean updateBySQL(String sql) throws SQLException {
        boolean result = true;
        try{
            System.out.println("updateBySQL:[" + sql + "]");
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            int num = statement.executeUpdate();
            if(num == 0){
                result = false;
            }
            statement.close();
            conn.commit();
        }catch(SQLException e){
            result = false;
            conn.rollback();
        }finally{
            conn.setAutoCommit(true);
        }
        return result;
    }
    
    /**通过存数更新指定数据**/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean update(String tableName,UpdatePara para) throws SQLException{
        String sql = "update " + tableName + " set ";
        Map<String,Object> map = para.getMap();
        Set<String> set = map.keySet();
        int size = map.size();
        int current = 0;
        for (String key : set) {
            Object o = map.get(key);
            String value = ""; 
            if(o instanceof String){
                value = "'" + o.toString() + "'";
            }else{
                value = o.toString(); 
            }
            sql = sql + key + "=" + value; 
            if(current < size - 1){
                sql += ",";
            }else{
                sql += " ";
            }
            current++;
        }
        sql += " where 1=1 ";
        while(para.hasNext()){
            UpdateParaItem upi = para.get();
            String columnName = upi.getColumnName().toString();
            String value = "";
            Object o = upi.getValue();
            if(o instanceof String){
                value = "'" + o.toString() + "'";
            }else{
                value = o.toString(); 
            }
            String method = upi.getMethod().toString();
            String andor = upi.getAndOr().toString();
            sql = sql + andor + " " + columnName + " " + method + " " + value + " ";
        }
        return updateBySQL(sql);
    };
    
    /**通过sql语句进行查询**/
    @Override
    public List<Map<String,String>> selectBySQL(String sql) throws SQLException {
        System.out.println("selectBySQL:SQL语句：[" + sql + "]");
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet set = statement.executeQuery();
        ResultSetMetaData md = set.getMetaData();
        int count = md.getColumnCount();
        while(set.next()){
            Map<String,String> line = new LinkedHashMap<String, String>();
            for(int i = 1;i <= count;i++){
                String columnName = md.getColumnName(i);
                String value = set.getString(columnName);
                line.put(columnName, value);
            }
            list.add(line);
        }
        return list;
    }
    
    /**从tableName中找出所有项**/
    @Override
    public List<Map<String,String>> select(String tableName) throws SQLException {
        String sql = "select * from " + tableName;
        return selectBySQL(sql);
    }
    
    /**从tableName中找出列名为columnName的项**/
    @Override
    public List<Map<String,String>> select(String tableName, String[] columnName) throws SQLException {
        String sql = "select ";
        int len = columnName.length;
        for(int i = 0;i < len;i++){
            sql += columnName[i];
            if(i == len -1){
                sql += " ";
            }else{
                sql += ",";
            }
        }
        sql = sql + "from " +  tableName;
        return selectBySQL(sql);
    }
    
    /**从tableName中找出列名为columnName且满足条件的的项,rule中的key表示字段，value表值**/
    @Override
    public List<Map<String,String>> select(String tableName, String[] columnName, Map<String, String> rule) throws SQLException {
        return select(tableName,columnName,rule,false);
    }
    
    /**从tableName中找出列名为columnName且满足条件的的项;rule中的key表示字段，value表值;isLike表示是否使用Like查询**/
    @Override
    public List<Map<String,String>> select(String tableName, String[] columnName, Map<String, String> rule, boolean isLike) throws SQLException {
        String sql = "select ";
        int len = columnName.length;
        for(int i = 0;i < len;i++){
            sql += columnName[i];
            if(i == len -1){
                sql += " ";
            }else{
                sql += ",";
            }
        }
        sql = sql + "from " +  tableName + " where ";
        Set<String> set = rule.keySet();
        int size = set.size();
        int current = 0;
        for (String key : set) {
            String value = rule.get(key);
            sql += key;
            if(isLike){
                sql += " like ";
            }else{
                sql += "=";
            }
            sql += value;
            if(current != size - 1){
                sql += " and ";
            }else{
                sql += " ";
            }
            current ++;
        }
        return selectBySQL(sql);
    }
    
    /**通过参数在tableName中找出项**/
    @SuppressWarnings("rawtypes")
    @Override
    public List<Map<String,String>> select(String tableName, String[] columnName, SelectPara para) throws SQLException {
        String sql = "select ";
        int len = columnName.length;
        for(int i = 0;i < len;i++){
            sql += columnName[i];
            if(i == len -1){
                sql += " ";
            }else{
                sql += ",";
            }
        }
        sql = sql + "from " +  tableName + " where 1=1 ";
        while(para.hasNext()){
            SelectParaItem p = para.get();
            String name = p.getColumnName().toString();
            String value = p.getValue().toString();
            String method = p.getMethod().toString();
            String andor = p.getAndOr().toString();
            sql = sql + andor + " " + name + " " + method + " " + value + " ";
        }
        return selectBySQL(sql);
    }
    
    /**
     * DCL
     * **/
    @Override
    public void grant() throws SQLException{
        
    };
    @Override
    public void deny() throws SQLException{
        
    };
    @Override
    public void revoke() throws SQLException{
        
    }
    
    
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
   /**释放数据库链接**/
    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return this.dbName;
    };
    
    
}

