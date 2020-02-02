package com.skjanyou.mvc.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.simplehttpserver.http.HttpRequest;
import com.skjanyou.server.simplehttpserver.http.HttpRequest.HttpRequestLine;

public class CharacterEncodingFilter implements Filter {

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public Filter init() {
		return this;
	}

	@Override
	public boolean doFilter(Request request, Response response)
			throws Exception {
		if( request instanceof HttpRequest ){
			HttpRequest httpRequest = (HttpRequest) request;
			String url = httpRequest.requestLine().url();
			try {
				url = URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if( httpRequest.requestLine() instanceof HttpRequestLine ){
				HttpRequestLine hrl = (HttpRequestLine) httpRequest.requestLine();
				hrl.setUrl(url);
			}
		}
		return true;
	}

	@Override
	public Filter destroy() {
		return null;
	}

}
