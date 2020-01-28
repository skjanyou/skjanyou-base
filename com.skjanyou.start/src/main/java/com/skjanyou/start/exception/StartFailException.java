package com.skjanyou.start.exception;

public class StartFailException extends RuntimeException {
	private static final long serialVersionUID = -2301294727075022211L;

	public StartFailException() {
		super();
	}

	public StartFailException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StartFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public StartFailException(String message) {
		super(message);
	}

	public StartFailException(Throwable cause) {
		super(cause);
	}
	
}
