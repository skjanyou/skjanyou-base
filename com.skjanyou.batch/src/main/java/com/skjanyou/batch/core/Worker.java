package com.skjanyou.batch.core;

import java.util.Map;

import com.skjanyou.batch.bean.BatchData;
import com.skjanyou.batch.exception.BatchException;

public interface Worker<I,O> {
	public BatchData<O> processAll( BatchData<I> batchData ) throws BatchException;
	public O process( I data,int index ) throws BatchException;
	public boolean processHandlerException( I data,int index,Exception e );
	public void afterWorkProcessAll( BatchData<I> batchData );
	public void afterWorkProcessAllHandlerException( int sucess,int fail, Map<Integer,Throwable> throwable,BatchData<I> inputBatchData,BatchData<O> outputBatchData );             
	public void beforeWorkProcessAll( BatchData<I> batchData );
	public void beforeWorkProcessAllHandlerException( Exception e );
}
