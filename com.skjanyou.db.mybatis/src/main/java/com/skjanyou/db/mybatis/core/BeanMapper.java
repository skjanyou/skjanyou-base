package com.skjanyou.db.mybatis.core;

import java.util.List;

import com.skjanyou.db.mybatis.bean.Page;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Insert;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Query;
import com.skjanyou.db.mybatis.core.BeanAnnotation.QueryPage;
import com.skjanyou.db.mybatis.core.BeanAnnotation.SelectFirst;
import com.skjanyou.db.mybatis.core.BeanAnnotation.SelectOne;
import com.skjanyou.db.mybatis.core.BeanAnnotation.Update;

public interface BeanMapper<T> {
	@SelectOne
	public T selectOne( T selectEntity );
	@SelectFirst
	public T selectFirst( T selectEntity );
	@Insert
	public Integer insert( T entity );
	@Query
	public List<T> query();
	@QueryPage
	public List<T> queryPage( Page<T> entity );
	@Update
	public Integer update( T entity );
	
}