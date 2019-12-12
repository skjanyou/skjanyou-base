package com.skjanyou.server.impl.http;

import java.util.Map;

import com.skjanyou.server.inter.Protocol;
import com.skjanyou.server.inter.Request.RequestLine;

public class HttpRequestLine implements RequestLine {
	private String method;
	private String url;
	private Protocol protocol;
	private Map<String,Object> params;
	
	@Override
	public String method() {
		return this.method;
	}

	@Override
	public String url() {
		return this.url;
	}

	@Override
	public Protocol protocol() {
		return this.protocol;
	}

	@Override
	public Map<String, Object> queryParams() {
		return this.params;
	}

	@Override
	public RequestLine convertToRequestLine(String firstLine) {
        String[] parm = firstLine.split("\\s");
        String method = parm[0];
        String uri = parm[1];		
        this.method = method;
        this.url = uri;
		return this;
	}
}
