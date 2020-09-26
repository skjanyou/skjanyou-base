package com.skjanyou.batch.core.inter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.skjanyou.batch.bean.BatchData;
import com.skjanyou.batch.core.Worker;
import com.skjanyou.batch.exception.BatchException;

public abstract class DispatchableWorker<I,O> extends DefaultWorker<I, O> {
	private int threadCount = 50;
	private LinkedList<Worker<I,O>> workList = null;
	private int mills = 1000;
	private CountDownLatch latch = null;
	private ExecutorService es = null;
	public DispatchableWorker( int threadCount ) {
		if( threadCount > 0 ) {
			this.threadCount = threadCount;
		}
		this.init();
	}
	
	private void init() {
		this.workList = new LinkedList<>();
		this.es = Executors.newFixedThreadPool(this.threadCount);
		Worker<I,O> work = null;
		for( int i = 0;i < this.threadCount;i++ ) {
			work = new SimpleWorker<I,O>() {
				@Override
				public O process(I data, int index) throws BatchException {
					return DispatchableWorker.this.process(data, index);
				}
			};
			this.workList.add(work);
		}
	}
	
	private Worker<I,O> getWorker() throws InterruptedException{
		synchronized ( this.workList ) {
			if( this.mills <= 0 ) {
				while( this.workList.isEmpty() ) {
					this.workList.wait();
				}
				return this.workList.removeFirst();
			}else {
				long future = System.currentTimeMillis() + this.mills;
				long remaining = this.mills;
				while( this.workList.isEmpty() && remaining > 0 ) {
					this.workList.wait(remaining);
					remaining = future - System.currentTimeMillis();
				}
				
				return this.workList.isEmpty() ? null : workList.removeFirst();
			}
		}
	}
	
	private void releaseWork( Worker<I,O> worker ) {
		if( worker != null ) {
			synchronized ( this.workList ) {
				this.workList.addLast(worker);
				this.workList.notifyAll();
			}
		}
	}
	
	@Override
	public BatchData<O> processAll(BatchData<I> batchData) throws BatchException {
		BatchData<O> outputBatchData = new BatchData<>();
		this.latch = new CountDownLatch(batchData.getSize());
		
		try {
			this.beforeWorkProcessAll(batchData);
		} catch (Exception e) {
			this.beforeWorkProcessAllHandlerException(e);
		}
		
		boolean hasException = false;
		Map<Integer,Throwable> throwable = new HashMap<>();
		int success = 0; int fail = 0;
		I data = null;O output = null;
		int index = 0;
		Worker<I,O> worker = null;WorkerRunnable<I, O> wr = null;
		List<WorkerRunnable<I,O>> wrList = new ArrayList<>();
		while( batchData.hasNext() ) {
			data = batchData.next();
			index = batchData.getIndex();
			try {
				worker = getWorker();
				wr = new WorkerRunnable<>(worker,data,index,this.latch);
				wrList.add(wr);		
			}catch( Exception e ) {
				fail++;
				hasException = true;
				this.processHandlerException(data, index, e);
				throwable.put(index, e);
			}
			
		}
		// 不再加入线程
		this.es.shutdownNow();
		try {
			List<Future<O>> futureList = this.es.invokeAll(wrList);
			Future<O> future = null;WorkerRunnable<I, O> wr2 = null;
			
			for( int i = 0; i < futureList.size();i++ ) {
				future = futureList.get(i);
				wr2 = wrList.get(i);
				try {
					output = future.get();
					outputBatchData.add(output);
				} catch (ExecutionException e) {
					continue;
				} finally {
					releaseWork(wr2.getWorker());
				}
				
			}
		} catch (InterruptedException e) {
			
		}
		if( hasException ) {
			this.afterWorkProcessAllHandlerException(success, fail, throwable, batchData, outputBatchData);
		}
		this.afterWorkProcessAll(batchData);
		
		try {
			this.latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return outputBatchData;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
