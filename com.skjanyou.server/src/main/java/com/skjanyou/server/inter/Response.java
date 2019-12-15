package com.skjanyou.server.inter;

import java.io.File;
import java.io.OutputStream;

import com.skjanyou.server.constant.StatusCode;

public interface Response {
	public ResponseBody responseBody();
	public ResponseLine responseLine();
	public Headers headers();
	
	
	public static interface ResponseBody{
		public ResponseBody setBodyContent(String bodyContent);
		public ResponseBody setBodyContent(byte[] bodyContent);
		public ResponseBody setBodyContent(File file);
		public byte[] getBodyContent();
		public ResponseBody writeToStream( OutputStream os );
	}
	
	public static interface ResponseLine{
		public StatusCode statusCode();
		public Protocol protocol();
	}
}
