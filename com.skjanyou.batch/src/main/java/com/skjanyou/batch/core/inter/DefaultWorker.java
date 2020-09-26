package com.skjanyou.batch.core.inter;

import java.util.HashMap;
import java.util.Map;

import com.skjanyou.batch.bean.BatchData;
import com.skjanyou.batch.bean.BatchStatus;
import com.skjanyou.batch.core.Worker;
import com.skjanyou.batch.exception.BatchException;

public abstract class DefaultWorker<I, O> implements Worker<I, O> {
	
	@Override
	public BatchData<O> processAll(BatchData<I> batchData) throws BatchException {
		BatchData<O> outputBatch = new BatchData<>();
		try {
			this.beforeWorkProcessAll(batchData);
		} catch ( Exception e ) {
			this.beforeWorkProcessAllHandlerException(e);
		}
		
		boolean hasException = false;
		Map<Integer,Throwable> throwable = new HashMap<>();
		int success = 0; int fail = 0;
		I data = null;O output = null;
		int index = 0;
		
		while( batchData.hasNext() ) {
			data = batchData.next();
			index = batchData.getIndex();
			try {
				output = this.process(data, index);
				outputBatch.add(output);
				success++;
			} catch ( Exception e ) {
				fail++;
				hasException = true;
				this.processHandlerException(data, index, e);
				throwable.put(index, e);
				
				if( e instanceof BatchException ) {
					BatchException be = (BatchException) e;
					if( be.getStatus() == BatchStatus.STOP ) {
						return outputBatch;
					}else if( be.getStatus() == BatchStatus.CONTINUE ) {
						
					}else if( be.getStatus() == BatchStatus.ERROR ) {
						throw new BatchException(e);
					}
					
				}else {
					throw new BatchException(e);
				}
				
			}
			
		}
		return outputBatch;
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
