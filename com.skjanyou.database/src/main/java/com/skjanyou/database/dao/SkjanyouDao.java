package com.skjanyou.database.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.appl.AppAnnotationloader.util.AnnotationUtil;
import com.skjanyou.database.anno.Cell;
import com.skjanyou.database.anno.PrimaryKey;
import com.skjanyou.database.anno.Table;

public interface SkjanyouDao<T> {
	
	public boolean insert(T entity);
	
	public Integer delete(T condition);
	
	public Integer update(T condition);
	
	public T detail(T condition);
	
	public List<T> query(T condition);
	
	public List<T> queryByPage(T condition,long start , long end);
	
	public default String genSQL(Class<?> clazz){
		String result = null;
		// 1.获取类与表的映射关系
		Table table = AnnotationUtil.getTypeAnnotation(clazz, Table.class);
		String tableName = table.alias();
		// 2.获取主键
		List<Method> primaryKeyMethodList = AnnotationUtil.getMethodByAnnotation(clazz, PrimaryKey.class);
		List<String> primaryKeyList = new ArrayList<String>();
		for(Method m : primaryKeyMethodList){
			PrimaryKey key = AnnotationUtil.getMethodAnnotation(m, PrimaryKey.class);
			primaryKeyList.add(key.alias());
		}
		// 3.获取类中属性与表字段的映射关系
		List<Method> cellMethodList = AnnotationUtil.getMethodByAnnotation(clazz, Cell.class);
		Map<String,String> cellMap = new HashMap<String,String>();
		for(Method m : cellMethodList){
			Cell cell = AnnotationUtil.getMethodAnnotation(m, Cell.class);
			cellMap.put(cell.alias(),cell.type());
		}
		
		
		return result;
	}
}
