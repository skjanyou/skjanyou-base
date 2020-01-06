package com.skjanyou.server.simplehttpserver.http;

import com.skjanyou.server.api.inter.Protocol;

public class HttpProtocolLv1 implements Protocol {

	@Override
	public String version() {
		return "1.1";
	}
	
	@Override
	public String protocol() {
		return "http";
	}

}
