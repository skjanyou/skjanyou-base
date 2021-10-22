package com.skjanyou.util.plus.session;

import java.util.List;
import java.util.Map;

public interface SqlSession extends DataSource{
	// 获取mapper 
	public<T> T getMapper( Class<T> clazz );
	// 获取mapper 
	public<T> T getMapper( Class<T> clazz,SqlSession session );	
	// 获取解析后的sql
	public<Input> String doResolveSql( String sql, Input input );
	// 获取Sql解析器
	public SqlResolver getSqlResolver();
	
	
	/**   新增记录       **/
	// 新增一条记录
	public<T> int insert( String sql, T input );
	// 新增多条记录	
	public<T> int insertList( String sql, List<T> dataList );
	// 通过Map新增
	public<T> int insertMap( String sql,Map<String,Object> dataMap );
	
	/**   删除记录  **/
	// 通过sql删除
	public int delete( String sql );
	// 通过条件删除记录
	public<T> int deleteByCondition( String sql, T input);
	
	
	/**   查询记录  **/
	// 查询列表sql
	public<I,O> List<O> selectList( String sql, I input );
	// 查询列表sql
	public<I,O> O selectOne( String sql, I input );	
	// 查询列表sql,带分页
	public<I,O> List<O> selectListByPage( String sql, I input, long start, long size );
	// 查询结果为Map
	public<I,O,R> Map<I,O> selectMap( String sql, R input ); 
	// 查询结果为List<Map>
	public<I,O,R> List<Map<I,O>> selectMapList( String sql, R input ); 	
	
	/**   修改记录  **/
	// 通过sql语句更新数据
	public int update( String sql );
	// 通过条件进行更新数据
	public<T> int updateByCondition( String sql, T input );
}
