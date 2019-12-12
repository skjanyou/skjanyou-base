package com.skjanyou.server.impl.http;

import com.skjanyou.server.inter.Request;
import com.skjanyou.server.inter.Response;
import com.skjanyou.server.inter.ServerHandler;

public class HttpServerHandler implements ServerHandler {

	@Override
	public boolean handler(Request request, Response response) {
		
		
		response.headers().put("Host", "localhost:2333");
		response.headers().put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");
		response.headers().put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		response.headers().put("Referer", "http://localhost:9999/jokes.html");
		
		return true;
	}

}
