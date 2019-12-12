package com.skjanyou.server.impl.http;

import java.io.InputStream;

import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Request;

public class HttpRequest implements Request {
	private Headers headers = new HttpHeaders();
	private RequestFeatures requestFeatures = new HttpRequestFeatures();
	private Requestbody requestbody = new HttpRequestbody();
	public HttpRequest( ){}
	
	
	public static class HttpRequestbody implements Requestbody {

		@Override
		public Requestbody readFromStream(InputStream is) {
			return null;
		}

		@Override
		public String getRequestbody() {
			return null;
		}
	}
	
	@Override
	public Requestbody requestBody() {
		return this.requestbody;
	}

	@Override
	public RequestFeatures requestFeatures() {
		return this.requestFeatures;
	}

	@Override
	public Headers headers() {
		return headers;
	}

}
