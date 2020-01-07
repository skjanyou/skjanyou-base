package com.skjanyou.server.api.exception;

public class ServerException extends RuntimeException{
	private static final long serialVersionUID = 7949873144131925002L;

	public ServerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServerException(String arg0) {
		super(arg0);
	}

	public ServerException(Throwable arg0) {
		super(arg0);
	}
	
}
