package com.skjanyou.protocol.core;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public interface ProtocolHandler {
	public URLConnection openConnection(URL u) throws IOException;
}
