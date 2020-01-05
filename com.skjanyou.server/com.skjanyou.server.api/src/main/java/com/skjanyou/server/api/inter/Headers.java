package com.skjanyou.server.api.inter;

public interface Headers {
	/**
	 * 获取一个头
	 * @param name
	 * @return
	 */
	public String get( String key );
	
	public Headers put( String key, String value ); 
	
	public Headers remove( String key );
	
	public int size();
	
	public String toHttpHeaderString();
	
	public Headers converToHeaders( String headers );
}
