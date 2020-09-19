package com.skjanyou.db.mybatis.inter;

import java.lang.annotation.Annotation;

import com.skjanyou.db.mybatis.bean.Invocation;

public interface SqlProcess<T extends Annotation> {
	public Object process( Invocation<T> pi ) throws Exception;
}
