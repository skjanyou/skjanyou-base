package com.skjanyou.server.simplehttpserver.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerContext {
	private static ThreadLocal<ServerContext> serverContext = new ThreadLocal<>();
	private Map<Object,Object> context = new ConcurrentHashMap<>();
	
	public static void set( ServerContext context) {
		serverContext.set(context);
	}
	
	public static ServerContext get() {
		ServerContext sc = serverContext.get();
		if( sc == null ) {
			sc = new ServerContext();
			set(sc);
		}
		return sc;
	}
	
	public<T> T getContext( Object obj ) {
		return (T) this.context.get(obj);
	}
	
	public void putContext( Object key, Object value ) {
		this.context.put(key, value);
	}
}
