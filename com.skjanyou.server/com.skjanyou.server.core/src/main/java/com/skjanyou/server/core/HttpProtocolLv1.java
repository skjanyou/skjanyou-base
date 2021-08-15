package com.skjanyou.server.core;

import com.skjanyou.server.api.inter.Protocol;

public class HttpProtocolLv1 implements Protocol {

	@Override
	public String version() {
		return "1.0";
	}
	
	@Override
	public String protocol() {
		return "HTTP";
	}

}
