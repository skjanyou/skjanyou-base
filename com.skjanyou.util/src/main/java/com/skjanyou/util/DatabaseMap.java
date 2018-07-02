package com.skjanyou.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DatabaseMap<K, V> implements Map<K, V> {
	private Connection connection = null;
	private String clazz = "org.sqlite.JDBC";
	private String file = null;
	private String driver = "jdbc:sqlite:";
	//判断表是否存在
	private static String TABLE_ISEXIST = "select count(*) from sqlite_master where type='table' and name = 'map'";
	//建表
	private static String CREATE_TABLE = "create table map(key varchar primary key,value varchar)";
	//删除表
	@SuppressWarnings("unused")
	private static String DROP_TABLE = "drop table map";
	//查询一行数据，查询条件为key
	private static String SELECT_ROW_KEY = "select key,value from map where key = ?";
	//查询一行数据，查询条件为value
	private static String SELECT_ROW_VALUE = "select key,value from map where value = ?";
	//查询所有数据
	private static String SELECT_ALL = "select key,value from map";
	//插入一条数据
	private static String INSERT_ROW = "insert into map(key,value) values(? , ?)";
	//更新一条数据
	private static String UPDATE_ROW = "update map set value = ? where key = ?";
	//删除一条数据
	private static String DELETE_ROW = "delete from map where key = ?";
	//删除表里面的数据
	private static String DELETE_ALL = "delete from map";
	
	public DatabaseMap(){
		this(null);
	}

	public DatabaseMap(String file) {
		if(file == null || "".equals(file)){
			file = "c:/map.tmp";
		}
		this.file = file;
		initDB();
	}

	//√
	protected void initDB(){
		try{
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
				System.out.println("已存在该表");
			}
		}
	}
	//√
	@Override
	public int size() {
		int count = 0;
		try{
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				count ++;
			}
			resultSet.close();
			statement.close();
		}catch(Exception e){
			throw new RuntimeException("获得size出错");
		}
		return count;
	}
	//√
	@Override
	public boolean isEmpty() {
		boolean result = false;
		try{
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = statement.executeQuery();
			int count = 0;
			while(resultSet.next()){
				count ++;
			}
			if(count > 0){
				result = true;
			}
			connection.commit();
			resultSet.close();
			statement.close();
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("获得isEmpty出错");
		}
		return result;
	}
	//√
	@Override
	public boolean containsKey(Object key) {
		boolean result = false;
		try{
			connection.setAutoCommit(false);
			String k = key.toString();
			PreparedStatement statement = connection.prepareStatement(SELECT_ROW_KEY);
			statement.setString(1, k);
			ResultSet resultSet = statement.executeQuery();
			int count = 0;
			while(resultSet.next()){
				count ++;
			}
			if(count > 0){
				result = true;
			}
			connection.commit();
			resultSet.close();
			statement.close();
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("获得containsKey出错");
		}
		return result;
	}
	//√
	@Override
	public boolean containsValue(Object value) {
		boolean result = false;
		try{
			connection.setAutoCommit(false);
			String v = value.toString();
			PreparedStatement statement = connection.prepareStatement(SELECT_ROW_VALUE);
			statement.setString(1, v);
			ResultSet resultSet = statement.executeQuery();
			int count = 0;
			while(resultSet.next()){
				count ++;
			}
			if(count > 0){
				result = true;
			}
			connection.commit();
			resultSet.close();
			statement.close();
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("获得size出错");
		}
		return result;
	}
	//√
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V value = null;
		try{
			String k = key.toString();
			PreparedStatement statement = connection.prepareStatement(SELECT_ROW_KEY);
			statement.setString(1, k);
			ResultSet resultSet = statement.executeQuery();
			value = (V) resultSet.getString("value");
			resultSet.close();
			statement.close();
		}catch(Exception e){
			String msg = e.getMessage();
			if(!"ResultSet closed".equals(msg)){
				throw new RuntimeException("get出错");				
			}
		}
		return value;
	}
	//√
	@Override
	public synchronized V put(K key, V value) {
		try{
			connection.setAutoCommit(false);
			String k = key.toString();
			String v = value.toString();
			PreparedStatement statement = connection.prepareStatement(INSERT_ROW);
			statement.setString(1, k);
			statement.setString(2, v);
			statement.executeUpdate();
			connection.commit();
			statement.close();
		}catch(Exception e){
			//			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			String msg = e.getMessage();
			if("column key is not unique".equals(msg)){
				try{
					connection.setAutoCommit(false);
					String k = key.toString();
					String v = value.toString();
					PreparedStatement statement = connection.prepareStatement(UPDATE_ROW);
					statement.setString(1, v);
					statement.setString(2, k);
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
		return value;
	}
	//√
	@Override
	public V remove(Object key) {
		V v = get(key);
		try{
			connection.setAutoCommit(false);
			String k = key.toString();
			PreparedStatement statement = connection.prepareStatement(DELETE_ROW);
			statement.setString(1, k);
			statement.executeUpdate();
			connection.commit();
			statement.close();
		}catch(Exception e){
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("remove出错");
		}
		return v;
	}
	//√直接一条一条插入，反正在jdbc中批量插入速度没有提升
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Set<? extends K> set = m.keySet();
		for (K key : set) {
			V value = m.get(key);
			put(key,value);
		}
	}
	//√
	@Override
	public void clear() {
		try{
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(DELETE_ALL);
			statement.executeUpdate();
			connection.commit();
			statement.close();
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("clear出错");
		}
	}
	//√
	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
		try{
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String key = resultSet.getString("key");
				set.add((K) key);
			}
			resultSet.close();
			statement.close();
		}catch(Exception e){
			throw new RuntimeException("获得keySet出错");
		}
		return set;
	}
	//√
	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Set<V> set = new LinkedHashSet<V>();
		try{
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String value = resultSet.getString("value");
				set.add((V) value);
			}
			resultSet.close();
			statement.close();
		}catch(Exception e){
			throw new RuntimeException("获得values出错");
		}
		return set;
	}
	//√
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Map<K,V> map = new HashMap<K, V>();
		Set<K> set = this.keySet();
		for (K key : set) {
			V value = this.get(key);
			map.put(key, value);
		}
		return map.entrySet();
	}


}
