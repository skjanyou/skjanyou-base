package com.skjanyou.protocol.core;

public interface ProtocolProvider {
	/** 
	  *  协议名 </br>
	  *  注: 
	 * classpath: 和 classpath:// 被认为是同一种协议,都会进入到Handler里面进行处理
	 * 
	 * **/
	public String protocol();
	/** 
	 *   获取协议处理
	 * @return
	 */
	public ProtocolHandler getHandler();
	
}
