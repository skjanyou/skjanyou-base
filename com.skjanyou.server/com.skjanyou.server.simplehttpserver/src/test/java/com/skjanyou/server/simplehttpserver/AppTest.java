package com.skjanyou.server.simplehttpserver;

import java.io.File;

import com.skjanyou.annotation.api.enumclass.ResponseType;
import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.Server;
import com.skjanyou.server.simplehttpserver.http.HttpHeaders;
import com.skjanyou.server.simplehttpserver.http.HttpRequest;
import com.skjanyou.server.simplehttpserver.http.HttpResponse;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseBody;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;
import com.skjanyou.server.simplehttpserver.http.HttpServer;
import com.skjanyou.server.simplehttpserver.http.HttpServerHandler;

public class AppTest {
	public static class MyServerHandler extends HttpServerHandler{

		@Override
		public void handler(HttpRequest request, HttpResponse response)
				throws ServerException {
			String method = request.requestLine().method();
			if( "POST".equals(method) ){
				System.out.println("POST请求,我要返回文件");
				response.headers().put("Content-Type", "zip");
				response.responseBody().setBodyContent(new File("D:\\学习\\x86.zip"));
			}else if("GET".equals(method)){
				System.out.println("GET请求,我要返回一个网页");
				response.headers().put("Content-Type", "xml/html");
				response.headers().put("Server", "SimpleHttpServer");
				response.responseBody().setBodyContent(new File("C:\\Users\\hasee\\Documents\\HBuilderProject\\skjanyou-web-music\\wallpaper.html"));
			}else{
				HttpResponseLine hrl =(HttpResponseLine) response.responseLine();;
				hrl.setStatusCode(StatusCode.Error);
				response.responseBody().setBodyContent("<p>服务器出现异常</p>");
				System.out.println("其他请求,我要返回状态码为错误");
			}			
		}

		@Override
		public void handlerException(Exception e, HttpRequest request,
				HttpResponse response) {
			// 响应行
			HttpResponseLine responseLine = response.getHttpResponseLine();
			// 响应体
			HttpResponseBody responseBody = response.getHttpResponseBody();
			// 响应头
			HttpHeaders httpHeaders = response.getHttpHeaders();
			
			responseLine.setStatusCode(StatusCode.Error);
			responseBody.setBodyContent("服务器内部错误");
			httpHeaders.put("Content-Type", ResponseType.HTML.getValue());			
		}
		
	}
	
	public static void main(String[] args) {
		ApplicateContext.setServerHandler(new MyServerHandler());
		ApplicateContext.registFilter(new Filter() {
			
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
				return true;
			}
			
			@Override
			public Filter destroy() {
				return null;
			}
		});
		ServerConfig config = new ServerConfig();
		config.setIp("127.0.0.1");config.setPort(2333);config.setTimeout(5000000);
		Server server = new HttpServer(config);
		server.startup();
	}
}
