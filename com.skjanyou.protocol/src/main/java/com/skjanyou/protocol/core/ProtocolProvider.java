package com.skjanyou.protocol.core;

import java.net.URLStreamHandler;

public interface ProtocolProvider {
	public String protocol();
	public URLStreamHandler getHandler();
	
}
