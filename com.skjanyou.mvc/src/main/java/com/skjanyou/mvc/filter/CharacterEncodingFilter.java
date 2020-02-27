package com.skjanyou.mvc.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
			String url = decode(httpRequest.requestLine().url());
			if( httpRequest.requestLine() instanceof HttpRequestLine ){
				HttpRequestLine hrl = (HttpRequestLine) httpRequest.requestLine();
				hrl.setUrl(url);
				Map<String,Object> params = hrl.queryParams();
				Map<String,Object> newParams = new HashMap<>();
				Set<String> paramsSet = params.keySet();
				for (String key : paramsSet) {
					newParams.put( decode(key),decode(params.get(key).toString()) );
				}
				hrl.setParams(newParams);
			}
		}
		return true;
	}

	@Override
	public Filter destroy() {
		return null;
	}

	private String decode( String input ){
		String output = input;
		try {
			output = URLDecoder.decode(input, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return output;
	}
}
