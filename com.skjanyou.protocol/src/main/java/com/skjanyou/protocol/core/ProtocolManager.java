package com.skjanyou.protocol.core;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

public class ProtocolManager {
	private static Map<String,ProtocolProvider> providerList = new HashMap<>();
	static{
		URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
			@Override
			public URLStreamHandler createURLStreamHandler(String protocol) {
				return providerList.get(protocol) == null ? null : providerList.get(protocol).getHandler();
			}
		});
	}
	
	public void registProtocolProvider( ProtocolProvider...protocolProviders ){
		for (ProtocolProvider protocolProvider : protocolProviders) {
			if( providerList.containsKey(protocolProvider.protocol()) ){
				throw new IllegalArgumentException(String.format("协议%s已存在处理器", protocolProvider.protocol()));
			}
			providerList.put(protocolProvider.protocol(), protocolProvider);
		}
	}
	
	public void unRegistProtocolProvider( ProtocolProvider...protocolProviders ){
		for (ProtocolProvider protocolProvider : protocolProviders) {
			if( !providerList.containsKey(protocolProvider.protocol()) ){
				throw new IllegalArgumentException(String.format("协议%s的处理器不存在", protocolProvider.protocol()));
			}
			providerList.remove(protocolProvider.protocol());
		}
	}
}
