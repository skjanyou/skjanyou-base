package com.skjanyou.db.mybatis.bean;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invocation<T> {
	private Class<?> mapperClass;
	private Object proxy;
	private Method method;
	private Object[] args;
	private T anno;
}
