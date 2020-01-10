package com.skjanyou.db.mybatis.inter;

import java.lang.annotation.Annotation;

import com.skjanyou.db.mybatis.bean.ProcessItem;

public interface SqlProcess<T extends Annotation> {
	public Object process( ProcessItem<T> pi );
}
