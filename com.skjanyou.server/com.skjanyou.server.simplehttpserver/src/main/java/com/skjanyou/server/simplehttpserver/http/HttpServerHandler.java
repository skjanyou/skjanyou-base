package com.skjanyou.server.simplehttpserver.http;

import java.io.File;

import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;

public class HttpServerHandler implements ServerHandler {

	@Override
	public boolean handler(Request request, Response response) {
		HttpRequest httpRequest = null;
		HttpResponse httpResponse = null;
		HttpResponseLine hrl = null;
		if( request instanceof HttpRequest && response instanceof HttpResponse ){
			httpRequest = (HttpRequest) request;
			httpResponse = (HttpResponse) response;
		}else{
			throw new RuntimeException("HttpServerHandler请求和响应类型有误!");
		}
		if(httpResponse.responseLine() instanceof HttpResponseLine){
			hrl = (HttpResponseLine) httpResponse.responseLine();
			hrl.setStatusCode(StatusCode.Ok);
			hrl.setProtocol(new HttpProtocolLv1());
		} 		
		
		String method = httpRequest.requestLine().method();
		if( "POST".equals(method) ){
			System.out.println("POST请求,我要返回文件");
			httpResponse.headers().put("Content-Type", "zip");
			httpResponse.responseBody().setBodyContent(new File("D:\\学习\\x86.zip"));
		}else if("GET".equals(method)){
			System.out.println("GET请求,我要返回一个网页");
			httpResponse.headers().put("Content-Type", "xml/html");
			httpResponse.headers().put("Server", "SimpleHttpServer");
			httpResponse.responseBody().setBodyContent(new File("C:\\Users\\hasee\\Documents\\HBuilderProject\\skjanyou-web-music\\wallpaper.html"));
		}else{
			hrl.setStatusCode(StatusCode.Error);
			httpResponse.responseBody().setBodyContent("<p>服务器出现异常</p>");
			System.out.println("其他请求,我要返回状态码为错误");
		}
		return true;
	}

	@Override
	public ServerHandler init() throws Exception {
		
		return this;
	}

}
