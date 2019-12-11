package com.skjanyou.server.impl.http;

import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Request;

public class HttpRequest implements Request {
	private Headers headers = new HttpHeaders();
	
	@Override
	public Requestbody requestBody() {
		return null;
	}

	@Override
	public RequestFeatures requestFeatures() {
		return null;
	}

	@Override
	public Headers headers() {
		return null;
	}

}
