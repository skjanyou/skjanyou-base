package com.skjanyou.protocol.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.skjanyou.protocol.core.ProtocolHandlerAndProvider;

public class SkjanyouProtocolProvider extends ProtocolHandlerAndProvider {
	@Override
	public URLConnection openConnection(URL url) throws IOException {
		return null;
	}

	@Override
	public String protocol() {
		return "skjanyou";
	}

}
