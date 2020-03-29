package com.skjanyou.server.api.inter;

import java.io.InputStream;
import java.util.Map;


public interface Request {
	/** 获取请求行 **/
	public RequestLine requestLine();
	/** 请求数据,仅POST请求有 **/
	public Requestbody requestBody();
	/** 获取请求头 **/
	public Headers headers();

	public static interface Requestbody{
		public Requestbody readFromStream( InputStream is );
		public String getRequestbody();
		public Requestbody convertToRequestbody( String formBody );
	}
	public static interface RequestLine{
	    String method();

	    String url();

	    Protocol protocol();

	    Map<String,Object> queryParams();
	    
	    RequestLine convertToRequestLine( String firstLine );
	}
}
