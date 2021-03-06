package com.skjanyou.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.server.api.inter.Headers;
import com.skjanyou.server.api.inter.Protocol;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.util.StreamUtil;
import com.skjanyou.util.convert.ConvertUtil;

public class HttpRequest implements Request {
	private HttpHeaders headers;
	private HttpRequestLine requestLine;
	private HttpRequestbody requestbody;
	public HttpRequest( ){
		this.headers = new HttpHeaders();
		this.requestLine = new HttpRequestLine(this);
		this.requestbody = new HttpRequestbody(this);
	}
	
	
	public static class HttpRequestbody implements Requestbody {
		private String requestBody;
		private HttpRequest request;
		private Map<String,Object> requestBodyMap;
		public HttpRequestbody(HttpRequest request){ 
			this.request = request;
			this.requestBodyMap = new HashMap<>();
		}
		
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
		
		public void setRequestBody(String requestBody) {
			this.requestBody = requestBody;
		}

		@Override
		public Requestbody convertToRequestbody(String formBody) {
			if( formBody != null && formBody.length() > 0 ){
				setRequestBody(formBody);
				// 这里有多种格式
				Object result = ConvertUtil.convert(formBody, Map.class);
				// 如果类型仍旧没有变化
				if( formBody.equals(result) ){
					String[] kvArr = formBody.split("&");
					for (String kv : kvArr) {
						String[] kvs = kv.split("=");
						if( kvs.length == 2 ){
							String key = kvs[0];
							String value = kvs[1];
							this.requestBodyMap.put(key, value);
						}
					}					
				}else{
					this.requestBodyMap.putAll((Map)result);
				}
				

			}
			return this;
		}

		public Map<String, Object> getRequestBodyMap() {
			return requestBodyMap;
		}
	}
	
	public static class HttpRequestLine implements RequestLine {
		private String method;
		private String url;
		private Protocol protocol;
		private Map<String,Object> params = new HashMap<String, Object>();
		private HttpRequest request;
		private HttpRequestLine(HttpRequest request){ this.request = request; }
		
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

		public void setUrl(String url) {
			this.url = url;
		}
		
		public void setMethod(String method) {
			this.method = method;
		}
		
		public void setProtocol(Protocol protocol) {
			this.protocol = protocol;
		}
		
		public void setParams(Map<String, Object> params) {
			this.params = params;
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

	public HttpHeaders getHttpHeaders() {
		return headers;
	}

	public HttpRequestLine getHttpRequestLine() {
		return requestLine;
	}

	public HttpRequestbody getHttpRequestbody() {
		return requestbody;
	}
}
