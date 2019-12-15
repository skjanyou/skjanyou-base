package com.skjanyou.server.impl.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Protocol;
import com.skjanyou.server.inter.Request;
import com.skjanyou.util.StreamUtil;

public class HttpRequest implements Request {
	private Headers headers = new HttpHeaders();
	private RequestLine requestLine = new HttpRequestLine(this);
	private Requestbody requestbody = new HttpRequestbody(this);
	public HttpRequest( ){}
	
	
	public static class HttpRequestbody implements Requestbody {
		private String requestBody;
		private Request request;
		public HttpRequestbody(Request request){ this.request = request; }
		
		@Override
		public Requestbody readFromStream(InputStream is) {
			String charset = request.headers().get("content-type");
			try {
				this.requestBody = StreamUtil.convertToString(is, charset, false);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("读取流异常");
			}
			return this;
		}

		@Override
		public String getRequestbody() {
			return this.requestBody;
		}
	}
	
	public static class HttpRequestLine implements RequestLine {
		private String method;
		private String url;
		private Protocol protocol;
		private Map<String,Object> params = new HashMap<String, Object>();
		private Request request;
		private HttpRequestLine(Request request){ this.request = request; }
		
		@Override
		public String method() {
			return this.method;
		}

		@Override
		public String url() {
			return this.url;
		}

		@Override
		public Protocol protocol() {
			return this.protocol;
		}

		@Override
		public Map<String, Object> queryParams() {
			return this.params;
		}

		@Override
		public RequestLine convertToRequestLine(String firstLine) {
	        String[] parm = firstLine.split("\\s");
	        String method = parm[0];
	        String uri = parm[1];		
	        
			String[] uriArr = uri.split("\\?");
			if( uriArr.length > 1 ){
				String parmString = uriArr[1];
				String[] kvArr = parmString.split("&");
				for (String kv : kvArr) {
					String[] kvs = kv.split("=");
					if( kvs.length == 2 ){
						String key = kvs[0];
						String value = kvs[1];
						this.params.put(key, value);
					}
				}
			}
			
	        this.method = method;
	        this.url = uri;
			return this;
		}
	}	
	
	@Override
	public Requestbody requestBody() {
		return this.requestbody;
	}

	@Override
	public RequestLine requestLine() {
		return this.requestLine;
	}

	@Override
	public Headers headers() {
		return headers;
	}

}
