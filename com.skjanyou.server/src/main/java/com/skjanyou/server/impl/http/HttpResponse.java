package com.skjanyou.server.impl.http;

import java.io.OutputStream;

import com.skjanyou.server.constant.StatusCode;
import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Protocol;
import com.skjanyou.server.inter.Response;

public class HttpResponse implements Response {
	private Headers headers = new HttpHeaders();
	private ResponseBody responseBody = new HttpResponseBody();
	private ResponseFeatures responseFeatures = new HttpResponseFeatures();
	
	public static class HttpResponseBody implements ResponseBody {

		@Override
		public byte[] getBodyContent() {
			return "<br>这是一个html元素</br>".getBytes();
		}

		@Override
		public ResponseBody writeToStream(OutputStream os) {
			return null;
		}
	} 
	
	public static class HttpResponseFeatures implements ResponseFeatures {

		@Override
		public StatusCode statusCode() {
			return StatusCode.Ok;
		}

		@Override
		public Protocol protocol() {
			return new Protocol() {
				
				@Override
				public String version() {
					return "1.1";
				}
				
				@Override
				public String protocol() {
					return "HTTP";
				}
			};
		}
	}
	
	@Override
	public ResponseBody responseBody() {
		return this.responseBody;
	}

	@Override
	public ResponseFeatures responsefeaFeatures() {
		return this.responseFeatures;
	}

	@Override
	public Headers headers() {
		return this.headers;
	}

}
