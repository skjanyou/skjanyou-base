package com.skjanyou.db.mybatis.inter;

import java.lang.annotation.Annotation;

import com.skjanyou.db.mybatis.bean.ProcessItem;

public interface AnnotationHandler<T extends Annotation> {
	public Object handler( ProcessItem<T> item );
}
