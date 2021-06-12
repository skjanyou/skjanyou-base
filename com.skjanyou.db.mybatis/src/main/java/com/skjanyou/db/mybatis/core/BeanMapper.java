package com.skjanyou.db.mybatis.core;

import java.util.List;

import com.skjanyou.db.mybatis.bean.KeyParams;
import com.skjanyou.db.mybatis.bean.Page;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Insert;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Query;
import com.skjanyou.db.mybatis.core.BeanAnnotation.QueryPage;
import com.skjanyou.db.mybatis.core.BeanAnnotation.SelectFirst;
import com.skjanyou.db.mybatis.core.BeanAnnotation.SelectOne;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Update;

/**
 * 基于Bean的CRUD操作
 *
 * @param <T> Bean的类型
 */
public interface BeanMapper<T> {
	/**
	 * 查询一条数据,当查询到的数据数量大于1时,会抛出异常
	 * @param selectEntity 查询条件
	 * @return 查询结果
	 */
	@SelectOne
	public T selectOne( T selectEntity );
	
	public T selectOneByKey( KeyParams keyParams );
	/**
	 * 查询第一条数据,当查询到的数据量大于1时,会取第一条,所以需要注意排序
	 * @param selectEntity 查询条件
	 * @return 查询结果
	 */
	@SelectFirst
	public T selectFirst( T selectEntity );
	/**
	 * 插入一条数据
	 * @param entity 要插入的数据
	 * @return	插入数据条数
	 */
	@Insert
	public Integer insert( T entity );
	/**
	 * 查询所有记录
	 * @return 返回所有的数据
	 */
	@Query
	public List<T> query();
	/**
	 * 分页查询
	 * @param entity 带分页的查询条件 
	 * @return 分页查询的结果
	 */
	@QueryPage
	public List<T> queryPage( Page<T> entity );
	/**
	 * 更新一条数据
	 * @param entity 更新数据 
	 * @return 更新数量
	 */
	@Update
	public Integer update( T entity );
	/**
	 * 删除记录
	 * @param entity 删除数据
	 * @return 删除数据条数
	 */
	public Integer delete( T entity );
	/**
	 * 通过主键删除记录
	 * @param keyParams  主键入参
	 * @return 删除数据条数
	 */
	public Integer deleteByKey( KeyParams keyParams );
	
	// 带总数的查询
	public List<T> selectPageWithCount( Page<T> condition );
	
	// 批量插入
	public Integer insertBatch( List<T> batchEntity);
	
	// 按照主键删除
	public Integer deleteByPrimaryKey( T condition );
	
	
}