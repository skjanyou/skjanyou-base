package com.skjanyou.protocol.core;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.protocol.entity.Resources;
import com.skjanyou.protocol.impl.ClasspathProtocolProvider;

public class Protocol {
	private static Map<String,ProtocolProvider> providerList = new HashMap<>();
	private static volatile boolean isInited = false;
	private static URLStreamHandlerFactory ProrocolURLStreamHandlerFactory = new URLStreamHandlerFactory() {
		@Override
		public URLStreamHandler createURLStreamHandler(String protocol) {
			return providerList.get(protocol) == null ? null : new URLStreamHandler() {
				@Override
				protected URLConnection openConnection(URL u) throws IOException {
					return providerList.get(protocol).getHandler().openConnection(u);
				}
			};
		}
	};
	
	public static void init() {
		if( isInited ) {
			return ;
		}
		URL.setURLStreamHandlerFactory(ProrocolURLStreamHandlerFactory);
		
		registProtocolProvider( new ClasspathProtocolProvider() );
	}
	
	public static void registProtocolProvider( ProtocolProvider...protocolProviders ){
		for (ProtocolProvider protocolProvider : protocolProviders) {
			if( providerList.containsKey(protocolProvider.protocol()) ){
				throw new IllegalArgumentException(String.format("协议%s已存在处理器", protocolProvider.protocol()));
			}
			providerList.put(protocolProvider.protocol(), protocolProvider);
		}
	}
	
	public static void unRegistProtocolProvider( ProtocolProvider...protocolProviders ){
		for (ProtocolProvider protocolProvider : protocolProviders) {
			if( !providerList.containsKey(protocolProvider.protocol()) ){
				throw new IllegalArgumentException(String.format("协议%s的处理器不存在", protocolProvider.protocol()));
			}
			providerList.remove(protocolProvider.protocol());
		}
	}
	
	public static Resources get( String link ) {
		return new Resources();
	}
}
