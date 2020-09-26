package com.skjanyou.batch.core.inter;

import java.util.Map;

import com.skjanyou.batch.bean.BatchData;
import com.skjanyou.batch.core.Worker;
import com.skjanyou.batch.exception.BatchException;

public abstract class SimpleWorker<I, O> implements Worker<I, O> {

	@Override
	public BatchData<O> processAll(BatchData<I> batchData) throws BatchException {
		return null;
	}

	@Override
	public O process(I data, int index) throws BatchException {
		return null;
	}

	@Override
	public boolean processHandlerException(I data, int index, Exception e) {
		return false;
	}

	@Override
	public void afterWorkProcessAll(BatchData<I> batchData) {
		
	}

	@Override
	public void afterWorkProcessAllHandlerException(int sucess, int fail, Map<Integer, Throwable> throwable,
			BatchData<I> inputBatchData, BatchData<O> outputBatchData) {
		
	}

	@Override
	public void beforeWorkProcessAll(BatchData<I> batchData) {
		
	}

	@Override
	public void beforeWorkProcessAllHandlerException(Exception e) {
		
	}

}
