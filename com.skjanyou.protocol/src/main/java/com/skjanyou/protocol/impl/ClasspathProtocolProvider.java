package com.skjanyou.protocol.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.skjanyou.protocol.core.ProtocolProvider;


public class ClasspathProtocolProvider extends URLStreamHandler implements ProtocolProvider {

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
        String path = u.getHost() + u.getPath();

        // Thread context class loader first
        URL classpathUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (classpathUrl == null) {
            // This class's class loader if no joy with the tccl
            classpathUrl = ClasspathProtocolProvider.class.getResource(path);
        }

        if (classpathUrl == null) {
            throw new FileNotFoundException(path);
        }

        return classpathUrl.openConnection();
	}

	@Override
	public String protocol() {
		return "classpath";
	}

	@Override
	public URLStreamHandler getHandler() {
		return this;
	}

}
