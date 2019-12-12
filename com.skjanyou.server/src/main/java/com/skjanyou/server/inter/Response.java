package com.skjanyou.server.inter;

import java.io.OutputStream;

import com.skjanyou.server.constant.StatusCode;

public interface Response {
	public ResponseBody responseBody();
	public ResponseFeatures responsefeaFeatures();
	public Headers headers();
	
	
	public static interface ResponseBody{
		public byte[] getBodyContent();
		public ResponseBody writeToStream( OutputStream os );
	}
	
	public static interface ResponseFeatures{
		public StatusCode statusCode();
		public Protocol protocol();
	}
}
