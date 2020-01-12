package com.skjanyou.db.mybatis.exception;

public class DaoException extends RuntimeException {
	private static final long serialVersionUID = 7204898906672115661L;

	public DaoException() {
		super();
	}

	public DaoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DaoException(String arg0) {
		super(arg0);
	}

	public DaoException(Throwable arg0) {
		super(arg0);
	}
	
}
