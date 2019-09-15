package com.skjanyou.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**ONP暂时是String类型**/
public class DatabaseONP {
	private Connection connection = null;
	private String clazz = "org.sqlite.JDBC";
	private String file = null;
	private String driver = "jdbc:sqlite:";
	//判断表是否存在
	private static String TABLE_ISEXIST = "select count(*) from sqlite_master where type='table' and name = 'onp'";
	//建表
	private static String CREATE_TABLE = "create table onp(oldmd5 varchar primary key,newmd5 varchar,oldlen varchar,newlen varchar,filename varchar,suffixes varchar,contenttype varchar,path varchar,date varchar)";
	//删除表
	@SuppressWarnings("unused")
	private static String DROP_TABLE = "drop table onp";
	//查询一行数据，查询条件为oldmd5
	private static String SELECT_ROW_OLD = "select * from onp where oldmd5 = ?";
	//查询一行数据，查询条件为newmd5
	private static String SELECT_ROW_NEW = "select * from onp where newmd5 = ?";
	//查询一行数据，查询条件为path
	private static String SELECT_ROW_PATH = "select * from onp where path = ?";
	//查询所有数据
	private static String SELECT_ALL = "select * from onp";
	//插入一条数据
	private static String INSERT_ROW = "insert into onp(oldmd5,newmd5,oldlen,newlen,filename,suffixes,contenttype,path,date) values(? , ? , ? , ? , ? , ? , ? , ? , ?)";
	//更新一条数据
	private static String UPDATE_ROW = "update onp set newmd5 = ?,oldlen = ?,newlen = ?,filename = ?,suffixes = ?,contenttype = ?,path = ?,date = ? where oldmd5 = ?";
	//删除一条数据
	private static String DELETE_ROW = "delete from onp where oldmd5 = ?";
	//删除表里面的数据
	private static String DELETE_ALL = "delete from onp";


	public class RowItem{
		private String old_md5;
		private String new_md5;
		private Long oldlen;
		private Long newlen;
		private String fileName;
		private String suffixes;
		private String contentType;
		private String path;
		private String date;
		
		public RowItem() {
		}
		
		public RowItem(String old_md5, String new_md5, Long oldlen,
				Long newlen, String fileName, String suffixes,
				String contentType, String path, String date) {
			super();
			this.old_md5 = old_md5;
			this.new_md5 = new_md5;
			this.oldlen = oldlen;
			this.newlen = newlen;
			this.fileName = fileName;
			this.suffixes = suffixes;
			this.contentType = contentType;
			this.path = path;
			this.date = date;
		}

		public String getOld_md5() {
			return old_md5;
		}
		public void setOld_md5(String old_md5) {
			this.old_md5 = old_md5;
		}
		public String getNew_md5() {
			return new_md5;
		}
		public void setNew_md5(String new_md5) {
			this.new_md5 = new_md5;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public Long getOldlen() {
			return oldlen;
		}
		public void setOldlen(Long oldlen) {
			this.oldlen = oldlen;
		}
		public Long getNewlen() {
			return newlen;
		}
		public void setNewlen(Long newlen) {
			this.newlen = newlen;
		}
		public String getSuffixes() {
			return suffixes;
		}
		public void setSuffixes(String suffixes) {
			this.suffixes = suffixes;
		}
		//oldmd5,newmd5,oldlen,newlen,filename,suffixes,contenttype,path,date
		@Override
		public String toString() {
			return "[old_md5:" + this.old_md5 + ",new_md5:" + this.new_md5 + ",oldlen:" + this.oldlen + ",newlen:" 
		           + this.newlen + ",filename:" + this.fileName + ",suffixes:" + this.suffixes + ",contentType:" 
				   + contentType + ",path:" + this.path + ",date:" + date + "]";
		}
	}
	public DatabaseONP(){
		this(null);
	}

	public DatabaseONP(String file){
		this.file = file;
		if(file == null || "".equals(file)){
			file = "c:/onp.tmp";
		}
		initDB();
	}

	//√
	protected void initDB(){
		try{
//			this.file = new String(this.file.getBytes(), "utf-8");
			Class.forName(clazz);
			connection = DriverManager.getConnection(this.driver + this.file);
			PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);
			statement.executeUpdate();
			statement.close();
		}catch(Exception e){
			String msg = e.getMessage();
			if(!msg.contains("exists")){
				throw new RuntimeException(e);				
			}else{
			}
		}
	}
	public RowItem getRow(String oldmd5){
		RowItem ri = null;
		try{
			PreparedStatement statement = connection.prepareStatement(SELECT_ROW_OLD);
			statement.setString(1, oldmd5);
			ResultSet resultSet = statement.executeQuery();
			String newmd5 = resultSet.getString("newmd5");
			long oldlen = Long.parseLong(resultSet.getString("oldlen"));
			long newlen = Long.parseLong(resultSet.getString("newlen"));
			String fileName = resultSet.getString("filename");
			String suffixes = resultSet.getString("suffixes");
			String contentType = resultSet.getString("contenttype");
			String path = resultSet.getString("path");
			String date = resultSet.getString("date");
			ri = new RowItem(oldmd5, newmd5, oldlen, newlen, fileName, suffixes, contentType, path, date);
			resultSet.close();
			statement.close();
		}catch(Exception e){
//			e.printStackTrace();
			String msg = e.getMessage();
			if(!"ResultSet closed".equals(msg)){
				throw new RuntimeException("getRow出错");				
			}
		}
		return ri;
	}

	public void putRow(RowItem ri){
		put(ri.getOld_md5(),ri.getNew_md5(),ri.getOldlen().toString(),ri.getNewlen().toString(),ri.getFileName(),ri.getSuffixes(),ri.getContentType(),ri.getPath(),ri.getDate());
	}
	public DatabaseONP put(String oldmd5,String newmd5,String oldlen,String newlen,String fileName,String suffixes,String contentType,String path,String date){
		try{
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(INSERT_ROW);
			statement.setString(1, oldmd5);
			statement.setString(2, newmd5);
			statement.setString(3, oldlen);
			statement.setString(4, newlen);
			statement.setString(5, fileName);
			statement.setString(6, suffixes);
			statement.setString(7, contentType);
			statement.setString(8, path);
			statement.setString(9, date);
			statement.executeUpdate();
			connection.commit();
			statement.close();
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			String msg = e.getMessage();
			if("column oldmd5 is not unique".equals(msg)){
				try{
					connection.setAutoCommit(false);
					PreparedStatement statement = connection.prepareStatement(UPDATE_ROW);
					statement.setString(1, newmd5);
					statement.setString(2, oldlen);
					statement.setString(3, newlen);
					statement.setString(4, fileName);
					statement.setString(5, suffixes);
					statement.setString(6, contentType);
					statement.setString(7, path);
					statement.setString(8, date);
					statement.setString(9, oldmd5);
					
					statement.executeUpdate();
					connection.commit();
					statement.close();
				}catch(Exception e1){
					throw new RuntimeException("更新值出错");
				}
			}else{
				throw new RuntimeException("put出错");				
			}
		}
		return this;
	}
}
