package com.skjanyou.protocol.core;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class SkjanyouURLStreamHandler extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return null;
	}

}
