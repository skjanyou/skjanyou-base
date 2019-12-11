package com.skjanyou.server.inter;

import java.util.Map;


public interface Request {
	public Requestbody requestBody();
	public RequestFeatures requestFeatures();
	public Headers headers();

	public static interface Requestbody{
		public String read( byte[] buff, int idx,int len );
		public String read();
	}
	public static interface RequestFeatures{
	    String method();

	    String url();

	    Protocol protocol();

	    Map<String,Object> queryParams();

	}
}
