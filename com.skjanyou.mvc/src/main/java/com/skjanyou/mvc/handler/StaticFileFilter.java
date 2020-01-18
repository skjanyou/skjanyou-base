package com.skjanyou.mvc.handler;

import java.io.File;

import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.simplehttpserver.http.HttpProtocolLv1;
import com.skjanyou.server.simplehttpserver.http.HttpRequest;
import com.skjanyou.server.simplehttpserver.http.HttpResponse;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;


// 静态资源过滤器
public class StaticFileFilter implements Filter {
	private String staticFilePath = null;
	public StaticFileFilter( String staticFilePath ){
		this.staticFilePath = staticFilePath;
	}
	
	@Override
	public int priority() {
		return 0;
	}

	@Override
	public Filter init() {
		return this;
	}

	@Override
	public boolean doFilter(Request request, Response response) throws Exception {
		HttpRequest httpRequest = (HttpRequest) request;
		HttpResponse httpResponse = (HttpResponse) response;
		HttpResponseLine httpResponseLine = (HttpResponseLine) httpResponse.responseLine();
		String url = httpRequest.requestLine().url();
		
		File f = new File( staticFilePath,url );
		httpResponse.responseBody().setBodyContent(f);
		httpResponseLine.setProtocol(new HttpProtocolLv1());
		httpResponseLine.setStatusCode(StatusCode.Ok);
		return false;
	}

	@Override
	public Filter destroy() {
		// TODO Auto-generated method stub
		return null;
	}

}
