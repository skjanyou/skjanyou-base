package com.skjanyou.batch.exception;

import com.skjanyou.batch.bean.BatchStatus;

public class BatchException extends Exception {
	private static final long serialVersionUID = -1634435885191972435L;
	private BatchStatus status = BatchStatus.ERROR;
	public BatchException() {
		super();
	}
	public BatchException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	public BatchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	public BatchException(String arg0) {
		super(arg0);
	}
	public BatchException(Throwable arg0) {
		super(arg0);
	}
	public BatchException( BatchStatus status ) {
		super();
		if( status == null ) { throw new IllegalArgumentException("status is null"); };
		this.status = status;
	}
	public BatchStatus getStatus() {
		return status;
	}
	public void setStatus(BatchStatus status) {
		this.status = status;
	}
	
}
