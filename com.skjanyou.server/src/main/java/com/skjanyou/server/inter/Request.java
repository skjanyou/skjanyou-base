package com.skjanyou.server.inter;

import java.io.InputStream;
import java.util.Map;


public interface Request {
	public Requestbody requestBody();
	public RequestFeatures requestFeatures();
	public Headers headers();

	public static interface Requestbody{
		public Requestbody readFromStream( InputStream is );
		public String getRequestbody();
	}
	public static interface RequestFeatures{
	    String method();

	    String url();

	    Protocol protocol();

	    Map<String,Object> queryParams();
	    
	    RequestFeatures convertToRequestFeatures( String firstLine );
	}
}
