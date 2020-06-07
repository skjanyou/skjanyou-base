package com.skjanyou.server.simplehttpserver.http;

import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpResponse;

public abstract class HttpServerHandler implements ServerHandler {

	public abstract void handler(HttpRequest request,HttpResponse response) throws ServerException;
	
	public abstract void handlerException( Exception e ,HttpRequest request, HttpResponse response );
	
	@Override
	public void handler(Request request, Response response) throws ServerException{
		HttpRequest httpRequest = null;
		HttpResponse httpResponse = null;
		if( request instanceof HttpRequest && response instanceof HttpResponse ){
			httpRequest = (HttpRequest) request;
			httpResponse = (HttpResponse) response;
		}else{
			handlerException(new ServerException("HttpServerHandler请求和响应类型有误!"),httpRequest,httpResponse);
		}
		try{
			handler(httpRequest,httpResponse);
		} catch( Exception e ) {
			handlerException(e,httpRequest,httpResponse);
		}	
	}

	@Override
	public ServerHandler init() throws ServerException {
		return this;
	}

}
