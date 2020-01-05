package com.skjanyou.server.simplehttpserver.http;

import java.io.File;

import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.Protocol;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;

public class HttpServerHandler implements ServerHandler {

	@Override
	public boolean handler(Request request, Response response) {
		String method = request.requestLine().method();
		if(response.responseLine() instanceof HttpResponseLine){
			HttpResponseLine hrl = (HttpResponseLine) response.responseLine();
			hrl.setStatusCode(StatusCode.Ok);
			hrl.setProtocol(new Protocol() {
				
				@Override
				public String version() {
					return "1.1";
				}
				
				@Override
				public String protocol() {
					return "http";
				}
			});
		} 		
		if( "POST".equals(method) ){
			System.out.println("POST请求,我要返回文件");
			response.headers().put("Content-Type", "zip");
			response.responseBody().setBodyContent(new File("D:\\学习\\x86.zip"));
		}else if("GET".equals(method)){
			System.out.println("GET请求,我要返回一个网页");
			response.headers().put("Content-Type", "xml/html");
			response.responseBody().setBodyContent(new File("C:\\Users\\hasee\\Documents\\HBuilderProject\\skjanyou-web-music\\wallpaper.html"));
		}else{
			System.out.println("其他请求,我要返回状态码为错误");

		}
		return true;
	}

	@Override
	public ServerHandler init() throws Exception {
		
		return this;
	}

}
