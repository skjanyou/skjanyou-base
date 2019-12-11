package com.skjanyou.server.inter;

import com.skjanyou.server.constant.StatusCode;

public interface Response {
	public ResponseBody responseBody();
	public ResponseFeatures responsefeaFeatures();
	public Headers headers();
	
	
	public static interface ResponseBody{
		public ResponseBody write( byte[] buff );
		public ResponseBody write( String context );
	}
	
	public static interface ResponseFeatures{
		public StatusCode statusCode();
		public Protocol protocol();
	}
}
