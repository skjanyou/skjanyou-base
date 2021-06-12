package com.skjanyou.mvc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MvcContext {
	private static ThreadLocal<MvcContext> mvcContext = new ThreadLocal<>();
	private Map<Object,Object> context = new ConcurrentHashMap<>();
	
	public static void set( MvcContext context) {
		mvcContext.set(context);
	}
	
	public static MvcContext get() {
		return mvcContext.get();
	}
	
	public<T> T getContext( Object obj ) {
		return (T) this.context.get(obj);
	}
	
	public void putContext( Object key, Object value ) {
		this.context.put(key, value);
	}
}
