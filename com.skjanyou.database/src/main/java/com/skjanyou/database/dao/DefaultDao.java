package com.skjanyou.database.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skjanyou.database.DBConstant;
import com.skjanyou.database.util.DbUtil;

public abstract class DefaultDao<T> implements SkjanyouDao<T> {
	private SqlSession session = DbUtil.getSession();
	
	@Override
	public boolean insert(T entity) {
		String className = entity.getClass().getName();
		String suffix = DBConstant.INSERT;
		String mapper = createMapper( className, suffix);
		int result = session.insert( mapper , entity);
		if(result > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public Integer delete(T condition) {
		String className = condition.getClass().getName();
		String suffix = DBConstant.DELETE;
		String mapper = createMapper( className, suffix);
		return session.delete( mapper , condition );
	}
	
	@Override
	public Integer update(T condition) {
		String className = condition.getClass().getName();
		String suffix = DBConstant.UPDATE;
		String mapper = createMapper( className, suffix);	
		return session.update(mapper, condition);
	}
	
	@Override
	public T detail(T condition) {
		String className = condition.getClass().getName();
		String suffix = DBConstant.DETAIL;
		String mapper = createMapper( className, suffix);	
		return session.selectOne(mapper, condition);
	}
	
	@Override
	public List<T> query(T condition) {
		String className = condition.getClass().getName();
		String suffix = DBConstant.QUERY;
		String mapper = createMapper( className, suffix);
		return session.selectList(mapper, condition);
	}
	
	@Override
	public List<T> queryByPage(T condition, long pageNo, long pageSize) {
		String className = condition.getClass().getName();
		String suffix = DBConstant.QUERY;
		String mapper = createMapper( className, suffix);
		Page<T> page = PageHelper.startPage((int)pageNo, (int)pageSize, true);
		session.selectList(mapper, condition);
		return page;
	} 

	private String createMapper(String beanPackage,String suffix){
		String p = beanPackage.replace(".bean", ".mapper");
		System.out.println(p);
		return String.format("%s%s", p, suffix);
	}
}
