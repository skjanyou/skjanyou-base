package com.skjanyou.protocol.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class SkjanyouProtocolProvider extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return null;
	}

}
