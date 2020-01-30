package com.skjanyou.server.simplehttpserver.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.Headers;
import com.skjanyou.server.api.inter.Protocol;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.util.FileUtil;

public class HttpResponse implements Response {
	private HttpHeaders headers ;
	private HttpResponseBody responseBody ;
	private HttpResponseLine responseLine ;
	
	public HttpResponse(){
		headers = new HttpHeaders();
		responseBody = new HttpResponseBody();
		responseLine = new HttpResponseLine();
	}
	
	public static class HttpResponseBody implements ResponseBody {
		private byte[] bodyContent;

		@Override
		public ResponseBody setBodyContent(String bodyContent) {
			this.bodyContent = bodyContent.getBytes();
			return this;
		}

		@Override
		public ResponseBody setBodyContent(byte[] bodyContent) {
			this.bodyContent = bodyContent;
			return this;
		}

		@Override
		public ResponseBody setBodyContent(File file) {
			this.bodyContent = FileUtil.getFileBytes(file);
			return this;
		}
		
		@Override
		public byte[] getBodyContent() {
			return this.bodyContent;
		}		

		@Override
		public ResponseBody writeToStream(OutputStream os) {
			try {
				os.write(this.bodyContent);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("写数据出错",e);
			}
			return this;
		}


	} 
	
	public static class HttpResponseLine implements ResponseLine {
		private StatusCode statusCode;
		private Protocol protocol;
		
		public void setStatusCode(StatusCode statusCode) {
			this.statusCode = statusCode;
		}
		
		public void setProtocol(Protocol protocol) {
			this.protocol = protocol;
		}
		
		@Override
		public StatusCode statusCode() {
			return this.statusCode;
		}

		@Override
		public Protocol protocol() {
			return this.protocol;
		}
	}
	
	@Override
	public ResponseBody responseBody() {
		return this.responseBody;
	}

	@Override
	public ResponseLine responseLine() {
		return this.responseLine;
	}

	@Override
	public Headers headers() {
		return this.headers;
	}
	
	/** 设置Http头 **/
	public HttpResponse putHeader( String key,String value ) {
		this.headers.put(key, value);
		return this;
	}
	
	/**  **/
	public HttpResponseBody getHttpResponseBody(){
		return this.responseBody;
	}
	
	public HttpResponseLine getHttpResponseLine(){
		return this.responseLine;
	}
	
	public HttpHeaders getHttpHeaders(){
		return this.headers;
	}
}
