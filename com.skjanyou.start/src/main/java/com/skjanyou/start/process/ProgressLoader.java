package com.skjanyou.start.process;

public interface ProgressLoader {
	public void progress( int total,int current,String message );
	public void done();
}
