package com.skjanyou.server.simplehttpserver.http;

import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.ServerHandler;

public abstract class HttpServerHandler implements ServerHandler {

	public abstract void handler(HttpRequest request,HttpResponse response) throws ServerException;
	
	@Override
	public void handler(Request request, Response response) throws ServerException{
		HttpRequest httpRequest = null;
		HttpResponse httpResponse = null;
		if( request instanceof HttpRequest && response instanceof HttpResponse ){
			httpRequest = (HttpRequest) request;
			httpResponse = (HttpResponse) response;
		}else{
			throw new ServerException("HttpServerHandler请求和响应类型有误!");
		}
		
		handler(httpRequest,httpResponse);
	}

	@Override
	public ServerHandler init() throws ServerException {
		return this;
	}

}
