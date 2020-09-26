package com.skjanyou.batch.core.inter;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.skjanyou.batch.core.Worker;

class WorkerRunnable<I,O> implements Callable<O> {
	private Worker<I,O> worker;
	private I inputBatchData;
	private int index;
	private CountDownLatch latch;
	
	public WorkerRunnable( Worker<I,O> worker, I inputBatchData,int index,CountDownLatch latch ) {
		this.worker = worker;
		this.inputBatchData = inputBatchData;
		this.index = index;
		this.latch = latch;
	}
	
	
	public Worker<I, O> getWorker() {
		return worker;
	}


	@Override
	public O call() throws Exception {
		O outputBatchData = null;
		try {
			outputBatchData = worker.process(this.inputBatchData, this.index);
		} finally {
			this.latch.countDown();
		}
		
		return outputBatchData;
	}

}
